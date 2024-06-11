package kz.services.romshop.controllers;

import kz.services.romshop.dto.SaleDTO;
import kz.services.romshop.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {
    private final SaleService service;
    @GetMapping("/all")
    public List<SaleDTO> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public SaleDTO get(@PathVariable Long id) { return service.get(id); }
}
