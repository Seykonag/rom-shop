package kz.services.romshop.controllers;

import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.dto.SaleDTO;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.services.CategoryService;
import kz.services.romshop.services.ProductService;
import kz.services.romshop.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/edit")
public class AdminEditController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @PostMapping("/category/{id}")
    public void editCategory(@PathVariable Long id, @RequestBody CategoryDTO request, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        categoryService.updateCategory(id, request);
    }

    @PostMapping("/product/{id}")
    public void editProduct(@PathVariable Long id, @RequestBody ProductDTO request, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        productService.upgradeProduct(id, request);
    }
}
