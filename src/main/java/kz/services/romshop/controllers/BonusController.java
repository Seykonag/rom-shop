package kz.services.romshop.controllers;

import kz.services.romshop.services.BonusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bonus")
public class BonusController {
    private final BonusService service;

    @GetMapping
    public BigDecimal myBonus(Principal principal) {
        return service.getBonus(principal.getName()).getSum();
    }
}
