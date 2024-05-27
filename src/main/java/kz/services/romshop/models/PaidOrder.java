package kz.services.romshop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "paid_orders")
public class PaidOrder {
    private static final String SEQ_NAME = "paid_order_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String paymentID;
    private String payerId;
    private String email;
    private String firstName;
    private String lastName;
    private String transactionId;
    private String currency;
    private String total;
    private String href;
    private LocalDateTime create;
    private LocalDateTime update;
}
