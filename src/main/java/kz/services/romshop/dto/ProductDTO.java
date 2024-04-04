package kz.services.romshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String model;
    private String developer;
    private byte[] photo;
    private boolean stock;
}
