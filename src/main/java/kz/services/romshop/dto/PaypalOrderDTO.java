package kz.services.romshop.dto;

import kz.services.romshop.models.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaypalOrderDTO {
    private Long id;
    private Long orderID;
    private String paymentID;
    private String payerId;
    private String email;
    private String firstName;
    private String lastName;
    private String transactionId;
    private String currency;
    private String total;
    private String href;
    private LocalDateTime created;
    private LocalDateTime updated;
}
