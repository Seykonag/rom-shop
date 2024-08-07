package kz.services.romshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal salePrice;
    private int percentageSale;
    private String model;
    private String developer;
    private String realPhoto;
    private byte[] photo;
    private boolean stock;
    private List<CommentDTO> comments;
    private Long categoryId;
}
