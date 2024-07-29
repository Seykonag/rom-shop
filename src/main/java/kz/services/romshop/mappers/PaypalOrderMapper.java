package kz.services.romshop.mappers;

import kz.services.romshop.dto.PaypalOrderDTO;
import kz.services.romshop.models.PaidOrder;
import org.springframework.stereotype.Component;

@Component
public class PaypalOrderMapper {
    public PaypalOrderDTO fromPaypalOrder(PaidOrder order) {
        return PaypalOrderDTO.builder()
                .id(order.getId())
                .orderID(order.getOrder().getId())
                .payerId(order.getPayerId())
                .paymentID(order.getPaymentID())
                .email(order.getEmail())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .transactionId(order.getTransactionId())
                .currency(order.getCurrency())
                .total(order.getTotal())
                .href(order.getHref())
                .created(order.getCreated())
                .updated(order.getUpdated())
                .build();
    }
}
