package kz.services.romshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsDTO {
    private String title;
    private Long productId;
    private BigDecimal price;
    private BigDecimal salePrice;
    private int percentageSale;
    private BigDecimal amount;
    private Double sum;
    private byte[] photo;
}
