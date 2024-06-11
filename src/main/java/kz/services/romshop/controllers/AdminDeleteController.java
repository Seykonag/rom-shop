package kz.services.romshop.controllers;

import kz.services.romshop.services.CategoryService;
import kz.services.romshop.services.ProductService;
import kz.services.romshop.services.SaleService;
import kz.services.romshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/delete")
public class AdminDeleteController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final SaleService saleService;

    @PostMapping("/user")
    public ResponseEntity<Boolean> deleteClient(@RequestBody List<Long> ids, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Не авторизованы");
        }

        boolean deleted = userService.delete(ids);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @PostMapping("/category")
    public ResponseEntity<Boolean> deleteCategory(@RequestBody List<Long> ids, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        boolean deleted = categoryService.deleteCategory(ids);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @PostMapping("/product")
    public ResponseEntity<Boolean> deleteProduct(@RequestBody List<Long> ids, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        boolean deleted = productService.deleteProduct(ids);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @PostMapping("/sale")
    public ResponseEntity<Boolean> deleteSale(@RequestBody List<Long> ids, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        boolean deleted = saleService.deleteSale(ids);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
