package kz.services.romshop.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import kz.services.romshop.dto.PaypalPayDTO;
import kz.services.romshop.services.OrderService;
import kz.services.romshop.services.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaypalController {
    private final PaypalService paypalService;
    private final OrderService orderService;

    @PostMapping
    public String paid(@RequestBody PaypalPayDTO request) {
        try {
            Payment payment = orderService.paidOrder(request);

            if (payment == null) return cancel();

            for (Links link: payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException exc) { throw new RuntimeException(exc); }

        return "All right";
    }

    @GetMapping("/cancel")
    public String cancel() { return "cancel"; }

    @GetMapping(value = "/success")
    public String success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String json = payment.toJSON();
            System.out.println(json);

            if (payment.getState().equals("approved")) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode rootNode = objectMapper.readTree(json);
                    JsonNode transactionsNode = rootNode.path("transactions");

                    if (transactionsNode.isArray()) {
                        for (JsonNode transactionNode : transactionsNode) {
                            JsonNode descriptionNode = transactionNode.path("description");
                            if (!descriptionNode.isMissingNode()) {
                                String description = descriptionNode.asText();
                                orderService.setOrderStatusPaid(Long.parseLong(description));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

}
