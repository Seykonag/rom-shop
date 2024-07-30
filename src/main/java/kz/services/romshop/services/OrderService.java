package kz.services.romshop.services;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.dto.PaypalPayDTO;
import kz.services.romshop.mappers.OrderMapper;
import kz.services.romshop.models.*;
import kz.services.romshop.repositories.OrderDetailsRepository;
import kz.services.romshop.repositories.OrderRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
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
    private final BonusService bonusService;
    private final OrderMapper orderMapper;
    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;


    @Transactional
    public void createOrder(String email, Map<Long, Integer> productID) {
        User user = userRepository.getReferenceByEmail(email);

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .address("Тестовый адрес")
                .build();

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

        for (Order order: orders) orderDTOS.add(orderMapper.mapToOrderDTO(order));

        return orderDTOS;
    }

    public List<OrderDTO> myOrders(String email) {
        User user = userRepository.getReferenceByEmail(email);

        return repository.findByUser(user).stream()
                .map(orderMapper::mapToOrderDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrderStatus(OrderStatus status) {
        List<OrderDTO> list = new ArrayList<>();
        List<Order> orders = repository.findByStatus(status);

        for (Order order: orders) {
            OrderDTO dto = orderMapper.mapToOrderDTO(order);
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

    public void confirmOrder(Map<Long, Boolean> confirms, String email) {
        User user = userRepository.getReferenceByEmail(email);
        Long key = confirms.keySet().iterator().next();
        Order order = repository.getReferenceById(key);

        if (order.getUser() != user) throw new RuntimeException("Это не ваш заказ");
        if (order.getStatus() != OrderStatus.NEW) throw new RuntimeException("Заказ не правильно обработан");

        if (confirms.get(key)) order.setStatus(OrderStatus.APPROVED);
        else order.setStatus(OrderStatus.CANCELED);

        repository.save(order);
    }

    @Transactional
    public String paidOrder(PaypalPayDTO dto) throws PayPalRESTException {
        Order order = repository.getReferenceById(dto.getIdOrder());

        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException("Заказ не одобрен");
        }

        order.setStatus(OrderStatus.PAID);
        repository.save(order);

        BigDecimal paidSum;
        if (dto.getBonus() != null && dto.getBonus()) {
            paidSum = bonusService.expendBonus(order);
        } else {
            paidSum = order.getSum();
        }

        if (paidSum == null || paidSum.compareTo(BigDecimal.ZERO) <= 0) {
            return "Заказ полностью оплачен бонусами";
        }

        BigDecimal paidSumInRUB = paidSum.multiply(new BigDecimal("0.17068"));
        Payment payment = paypalService.createPayment(
                paidSumInRUB.doubleValue(),
                "RUB",
                "paypal",
                "sale",
                order.getId().toString(),
                "https://rom-shop-0c9c08d95305.herokuapp.com/pay/cancel",
                "https://rom-shop-0c9c08d95305.herokuapp.com/pay/success"
        );

        if (payment == null) {
            return "Ошибка при создании платежа";
        }

        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }

        return "Все в порядке";
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

    public void editAddressOrder(Long id, String address) {
        Order order = repository.getReferenceById(id);
        order.setAddress(address);
        repository.save(order);
    }

    private List<OrderDetails> compileOrderDetails(Order order, Map<Long, Integer> products) {
        List<OrderDetails> list = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Product product = productRepository.getReferenceById(entry.getKey());
            if (!product.isStock()) continue;

            OrderDetails orderDetails = OrderDetails.builder()
                    .order(order)
                    .product(product)
                    .amount(new BigDecimal(entry.getValue()))
                    .price(
                            (product.getSalePrice() != null ? product.getSalePrice() : product.getPrice())
                    )
                    .build();

            list.add(orderDetails);
        }

        orderDetailsRepository.saveAll(list);
        return list;
    }

    public OrderDTO getOrderDto(Long id) {
        return orderMapper.mapToOrderDTO(repository.getReferenceById(id));
    }

    public Order getOrder(Long id) { return repository.getReferenceById(id); }
}