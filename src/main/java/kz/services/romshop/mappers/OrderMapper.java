package kz.services.romshop.mappers;

import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.dto.OrderDetailsDTO;
import kz.services.romshop.models.Order;
import kz.services.romshop.models.OrderDetails;
import kz.services.romshop.models.Sale;
import kz.services.romshop.utilits.CalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final CalculateUtils calculateUtils;

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
                        .percentageSale(Optional.ofNullable(
                                order.getProduct()
                                        .getCategories()
                                        .getSale()
                                )
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
}
