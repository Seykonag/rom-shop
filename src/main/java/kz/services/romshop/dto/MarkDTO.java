package kz.services.romshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarkDTO {
    private List<BucketDetailsDTO> bucketDetails = new ArrayList<>();

    public void formation() { singleProduct(); shortDTO(); }

    private void singleProduct() {
        Set<BucketDetailsDTO> set = new HashSet<>(this.bucketDetails);
        this.bucketDetails.clear(); this.bucketDetails.addAll(set);
    }

    private void shortDTO() {
        for (BucketDetailsDTO item: bucketDetails) {
            BucketDetailsDTO newItem = BucketDetailsDTO.builder()
                    .title(item.getTitle())
                    .productId(item.getProductId())
                    .price(item.getPrice())
                    .photo(item.getPhoto())
                    .amount(new BigDecimal(1))
                    .build();

            bucketDetails.remove(item);
            bucketDetails.add(newItem);
        }
    }
}
