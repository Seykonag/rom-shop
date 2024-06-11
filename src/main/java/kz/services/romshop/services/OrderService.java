package kz.services.romshop.services;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.dto.OrderDetailsDTO;
import kz.services.romshop.dto.PaypalPayDTO;
import kz.services.romshop.models.*;
import kz.services.romshop.repositories.OrderDetailsRepository;
import kz.services.romshop.repositories.OrderRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
import kz.services.romshop.utilits.CalculateUtils;
import kz.services.romshop.utilits.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final PaypalService paypalService;
    private final CalculateUtils calculateUtils;
    private final BonusService bonusService;
    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createOrder(String username, Map<Long, Integer> productID) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not user"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.NEW);
        order.setAddress(user.getAddress());

        List<OrderDetails> orderDetailsList = compileOrderDetails(order, productID);
        order.setDetails(orderDetailsList);
        order.setSum(orderDetailsList.stream()
                .map(OrderDetails::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        repository.save(order);
    }

    public List<OrderDTO> getAll() {
        List<Order> orders = repository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order: orders) orderDTOS.add(mapToOrderDTO(order));

        return orderDTOS;
    }

    public List<OrderDTO> myOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not user"));

        return repository.findByUser(user).stream()
                .map(this::mapToOrderDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrderStatus(OrderStatus status) {
        List<OrderDTO> list = new ArrayList<>();
        List<Order> orders = repository.findByStatus(status);

        for (Order order: orders) {
            OrderDTO dto = mapToOrderDTO(order);
            dto.setUsername(order.getUser().getUsername());
            list.add(dto);
        }

        return list;
    }

    public void confirmOrder(Map<Long, Boolean> confirms) {
        Set<Long> keys = confirms.keySet();

        for (Long key: keys) {
            Order order = repository.getReferenceById(key);

            if (order.getStatus() != OrderStatus.NEW) throw new RuntimeException("Заказ не правильно обработан");
            if (confirms.get(key)) order.setStatus(OrderStatus.APPROVED);
            else order.setStatus(OrderStatus.CANCELED);
            repository.save(order);
        }
    }

    public void confirmOrder(Map<Long, Boolean> confirms, String username) {
        User user = userRepository.getReferenceByUsername(username);
        Long key = confirms.keySet().iterator().next();

        Order order = repository.getReferenceById(key);
        if (order.getUser() != user) throw new RuntimeException("Это не ваш заказ");

        if (order.getStatus() != OrderStatus.NEW) throw new RuntimeException("Заказ не правильно обработан");
        if (confirms.get(key)) order.setStatus(OrderStatus.APPROVED);
        else order.setStatus(OrderStatus.CANCELED);
        repository.save(order);

    }

    @Transactional
    public Payment paidOrder(PaypalPayDTO dto) throws PayPalRESTException {
        Order order = repository.getReferenceById(dto.getIdOrder());

        if (order.getStatus() != OrderStatus.APPROVED) throw new RuntimeException("Заказ не одобрен");

        BigDecimal paidSum = bonusService.expendBonus(order);
        CurrencyConverter converter = new CurrencyConverter();
        BigDecimal paidSumInRUB = paidSum.multiply(new BigDecimal(0.19205));

        if (paidSum.compareTo(new BigDecimal(0)) > 0) {

            return paypalService.createPayment(
                    paidSumInRUB.doubleValue(),
                    "RUB",
                    "paypal",
                    "sale",
                    order.getId().toString(),
                    "http://localhost:8080/pay/cancel",
                    "http://localhost:8080/pay/success"
                    );
        }

        return null;
    }

    public void setOrderStatusPaid(Long id) {
        Order order = repository.getReferenceById(id);
        order.setStatus(OrderStatus.PAID);
        repository.save(order);
    }

    public void closedOrder(Long id) {
        Order order = repository.getReferenceById(id);

        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Заказ не оплачен");
        }

        order.setStatus(OrderStatus.CLOSED);

        bonusService.additionBonus(order.getUser(), order.getSum()
                .divide(new BigDecimal(10), RoundingMode.HALF_UP));

        repository.save(order);
    }

    public OrderDTO mapToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .phone(order.getUser().getPhone())
                .address(order.getAddress())
                .username(order.getUser().getUsername())
                .sum(order.getSum())
                .details(buildDetailsDTO(order.getDetails()))
                .created(order.getCreated())
                .updated(order.getUpdated())
                .status(order.getStatus())
                .build();
    }

    private List<OrderDetailsDTO> buildDetailsDTO(List<OrderDetails> orders) {
        return orders.stream()
                .map(order -> OrderDetailsDTO.builder()
                        .percentageSale(Optional.ofNullable(order.getProduct().getCategories().getSale())
                                .map(Sale::getSale)
                                .orElse(0))
                        .title(order.getProduct().getTitle())
                        .photo(order.getProduct().getPhoto())
                        .productId(order.getProduct().getId())
                        .amount(order.getAmount())
                        .price(order.getProduct().getPrice())
                        .salePrice(order.getProduct().getSalePrice())
                        .sum(calculateUtils.multiplyProduct(
                                order.getProduct().getSalePrice() != null ?
                                        order.getProduct().getSalePrice() :
                                        order.getProduct().getPrice(),
                                order.getAmount()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<OrderDetails> compileOrderDetails(Order order, Map<Long, Integer> products) {
        List<OrderDetails> list = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Product product = productRepository.getReferenceById(entry.getKey());
            if (!product.isStock()) continue;
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setProduct(product);
            orderDetails.setAmount(new BigDecimal(entry.getValue()));
            orderDetails.setPrice((product.getSalePrice() != null ? product.getSalePrice() : product.getPrice())
                    .multiply(new BigDecimal(entry.getValue())));
            list.add(orderDetails);
        }

        orderDetailsRepository.saveAll(list);
        return list;
    }

    public OrderDTO getOrderById(Long id) {
        return mapToOrderDTO(repository.getReferenceById(id));
    }

    public Order getOrder(Long id) {return repository.getReferenceById(id);}
}

