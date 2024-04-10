package kz.services.romshop.controllers;

import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.dto.SaleDTO;
import kz.services.romshop.services.AuthService;
import kz.services.romshop.services.CategoryService;
import kz.services.romshop.services.ProductService;
import kz.services.romshop.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/new")
@RequiredArgsConstructor
public class AdminNewController {
    private final AuthService authenticationService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SaleService saleService;

    @PostMapping("/user-admin")
    public void createAdmin(@RequestBody RegistrationDTO request) {
        authenticationService.signUp(request, true);
    }

    @PostMapping("/category")
    public void createCategory(@RequestBody CategoryDTO request) { categoryService.createCategory(request); }

    @PostMapping("/product")
    public void createProduct(@RequestBody ProductDTO request) { productService.createProduct(request); }

    @PostMapping("/sale")
    public void createSale(@RequestBody SaleDTO request) { saleService.createSale(request); }
}
