package kz.services.romshop.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import kz.services.romshop.models.PaidOrder;
import kz.services.romshop.repositories.PaidOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaidOrderService {
    private final OrderService orderService;
    private final PaypalService paypalService;
    private final PaidOrderRepository repository;

    @Transactional
    public String processPaypal(String paymentId, String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String json = payment.toJSON();
            System.out.println(json);


            if (payment.getState().equals("approved")) {
                ObjectMapper objectMapper = new ObjectMapper();
                PaidOrder paidOrder = null;

                try {
                    JsonNode rootNode = objectMapper.readTree(json);
                    JsonNode transactionsNode = rootNode.path("transactions");

                    if (transactionsNode.isArray()) {
                        for (JsonNode transactionNode : transactionsNode) {
                            JsonNode descriptionNode = transactionNode.path("description");
                            JsonNode amountNode = transactionNode.path("amount");
                            JsonNode relatedResourcesNode = transactionNode.path("related_resources").get(0).path("sale");
                            JsonNode payerInfoNode = rootNode.path("payer").path("payer_info");

                            if (!descriptionNode.isMissingNode() && !amountNode.isMissingNode() && !relatedResourcesNode.isMissingNode() && !payerInfoNode.isMissingNode()) {
                                String description = descriptionNode.asText();
                                String currency = amountNode.path("currency").asText();
                                String total = amountNode.path("total").asText();
                                String transactionId = relatedResourcesNode.path("id").asText();
                                String payerEmail = payerInfoNode.path("email").asText();
                                String firstName = payerInfoNode.path("first_name").asText();
                                String lastName = payerInfoNode.path("last_name").asText();

                                JsonNode linksNode = relatedResourcesNode.path("links");
                                String href = "";
                                for (JsonNode linkNode : linksNode) {
                                    if ("self".equals(linkNode.path("rel").asText())) {
                                        href = linkNode.path("href").asText();
                                        break;
                                    }
                                }
/*
                                paidOrder = PaidOrder.builder()
                                        .order(orderService.getOrderById(Long.parseLong(description)))
                                        .paymentID(paymentId)
                                        .payerId(payerId)
                                        .email(payerEmail)
                                        .firstName(firstName)
                                        .lastName(lastName)
                                        .transactionId(transactionId)
                                        .currency(currency)
                                        .total(total)
                                        .href(href)
                                        .create(LocalDateTime.parse(relatedResourcesNode.path("create_time").asText()))
                                        .update(LocalDateTime.parse(relatedResourcesNode.path("update_time").asText()))
                                        .build();
*/
                                orderService.setOrderStatusPaid(Long.parseLong(description));
                            }
                        }
                    }
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }

                if (paidOrder != null) {
                    repository.save(paidOrder);
                }

                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }
}
