package kz.services.romshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaypalPayDTO {
    private Long idOrder;
    private Double total;
    private String currency;
    private String method;
    private String description;
    private Boolean bonus;
}
