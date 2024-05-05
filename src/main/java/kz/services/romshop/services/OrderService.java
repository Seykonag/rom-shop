package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.dto.OrderDetailsDTO;
import kz.services.romshop.models.*;
import kz.services.romshop.repositories.OrderDetailsRepository;
import kz.services.romshop.repositories.OrderRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
import kz.services.romshop.utilits.CalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final CalculateUtils calculateUtils;
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

    public List<OrderDTO> myOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not user"));

        List<OrderDTO> list = repository.findByUser(user).stream()
                .map(this::mapToOrderDTO)
                .collect(Collectors.toList());

        return list;
    }

    private OrderDTO mapToOrderDTO(Order order) {
        return OrderDTO.builder()
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
                        .percentageSale(order.getProduct().getCategories().getSale().getSale())
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
}

