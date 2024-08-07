package kz.services.romshop.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import kz.services.romshop.dto.PaypalOrderDTO;
import kz.services.romshop.dto.PaypalPayDTO;
import kz.services.romshop.services.OrderService;
import kz.services.romshop.services.PaidOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaypalController {
    private final PaidOrderService paidOrderService;
    private final OrderService orderService;

    @PostMapping
    public String paid(@RequestBody PaypalPayDTO request) {
        try {
            return orderService.paidOrder(request);
        } catch (PayPalRESTException exc) {
            throw new RuntimeException(exc);
        }
    }

    @GetMapping("/cancel")
    public String cancel() { return "cancel"; }

    @GetMapping(value = "/success")
    public ResponseEntity<Void> success(@RequestParam("paymentId") String paymentId,
                                        @RequestParam("PayerID") String payerId) {
        paidOrderService.processPaypal(paymentId, payerId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://195.133.198.25:3000/myorders")
                .build();
    }

    @GetMapping("/all")
    public List<PaypalOrderDTO> getAll() { return paidOrderService.all();  }

}
