package kz.services.romshop.controllers;

import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService service;
    @GetMapping("/all")
    public List<CategoryDTO> getAll() { return service.getAll(); }
}
