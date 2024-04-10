package kz.services.romshop.controllers;

import kz.services.romshop.dto.MarkDTO;
import kz.services.romshop.services.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mark")
public class MarkController {
    private  final MarkService markService;

    @GetMapping
    public MarkDTO myMark(Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");
        return markService.getMarkByUsername(principal.getName());
    }
}
