package kz.services.romshop.dto;

import kz.services.romshop.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDetailsDTO {
    private Long id;
    private String title;
    private Long productId;
    private BigDecimal price;
    private BigDecimal salePrice;
    private int percentageSale;
    private BigDecimal amount;
    private Double sum;
    private byte[] photo;

    public BucketDetailsDTO(Product product) {
        if (product.getSalePrice() == null) {
            this.sum = Double.valueOf(product.getPrice().toString());
        } else {
            this.sum = Double.valueOf(product.getSalePrice().toString());
            this.percentageSale = product.getCategories().getSale().getSale();
        }

        this.id = product.getId();
        this.price = product.getPrice();
        this.title = product.getTitle();
        this.productId = product.getId();
        this.salePrice = product.getSalePrice();
        this.amount = new BigDecimal(1.0);
        this.photo = product.getPhoto();
    }
}
