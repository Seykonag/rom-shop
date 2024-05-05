package kz.services.romshop.dto;

import kz.services.romshop.models.OrderDetails;
import kz.services.romshop.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private LocalDateTime created;
    private LocalDateTime updated;
    private BigDecimal sum;
    private OrderStatus status;
    private Map<Long, Integer> idProducts;
    private List<OrderDetailsDTO> details;
}
