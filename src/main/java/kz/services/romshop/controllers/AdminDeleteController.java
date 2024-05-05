package kz.services.romshop.controllers;

import kz.services.romshop.services.CategoryService;
import kz.services.romshop.services.ProductService;
import kz.services.romshop.services.SaleService;
import kz.services.romshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/delete")
public class AdminDeleteController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final SaleService saleService;

    @GetMapping("/client/{id}")
    public void deleteClient(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        userService.delete(id);
    }

    @GetMapping("/category/{id}")
    public void deleteCategory(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        categoryService.deleteCategory(id);
    }

    @GetMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        productService.deleteProduct(id);
    }

    @GetMapping("/sale/{id}")
    public void deleteSale(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        saleService.deleteSale(id);
    }
}
