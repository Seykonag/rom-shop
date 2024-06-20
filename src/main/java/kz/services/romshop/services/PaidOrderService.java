package kz.services.romshop.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import kz.services.romshop.dto.PaypalOrderDTO;
import kz.services.romshop.models.PaidOrder;
import kz.services.romshop.repositories.PaidOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaidOrderService {
    private final OrderService orderService;
    private final PaypalService paypalService;
    private final PaidOrderRepository repository;

    @Transactional
    public String processPaypal(String paymentId, String payerId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
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

                                orderService.setOrderStatusPaid(Long.parseLong(description));

                                JsonNode linksNode = relatedResourcesNode.path("links");
                                String href = "";
                                for (JsonNode linkNode : linksNode) {
                                    if ("self".equals(linkNode.path("rel").asText())) {
                                        href = linkNode.path("href").asText();
                                        break;
                                    }
                                }

                                paidOrder = PaidOrder.builder()
                                        .order(orderService.getOrder(Long.parseLong(description)))
                                        .paymentID(paymentId)
                                        .payerId(payerId)
                                        .email(payerEmail)
                                        .firstName(firstName)
                                        .lastName(lastName)
                                        .transactionId(transactionId)
                                        .currency(currency)
                                        .total(total)
                                        .href(href)
                                        .created(LocalDateTime.parse(relatedResourcesNode.path("create_time").asText(), formatter))
                                        .updated(LocalDateTime.parse(relatedResourcesNode.path("update_time").asText(), formatter))
                                        .build();
                            }
                        }
                    }
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }

                if (paidOrder != null) {
                    repository.save(paidOrder);
                }

                return "http://localhost:3000/orders";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "ERROR";
    }

    public List<PaypalOrderDTO> all() {
        List<PaidOrder> orders = repository.findAll();
        List<PaypalOrderDTO> dtoList =  new ArrayList<>();

        for (PaidOrder order: orders) {
            dtoList.add(PaypalOrderDTO.builder()
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
                            .build());
        }

        return dtoList;
    }
}
