package kz.services.romshop.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import kz.services.romshop.dto.PaypalPayDTO;
import kz.services.romshop.services.OrderService;
import kz.services.romshop.services.PaidOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaypalController {
    private final PaidOrderService paidOrderService;
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
        return paidOrderService.processPaypal(paymentId, payerId);
    }

}
