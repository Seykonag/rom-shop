package kz.services.romshop.controllers;

import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.models.Product;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<ProductDTO> products() { return service.getAll(); }

    @GetMapping("/{id}")
    public ProductDTO productInfo(@PathVariable Long id) {
        return service.getProduct(id);
    }



    @GetMapping("/{id}/bucket")
    public void addBucket(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        service.addToUserBucket(id, principal.getName());
        System.out.println("Success");
    }

    @GetMapping("/{id}/mark")
    public void addMark(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        service.addToUserMark(id, principal.getName());
        System.out.println("Success mark");
    }

    @PostMapping("/getCategory")
    public List<ProductDTO> getCategory(@RequestParam String categoryName) {
        return service.findByCategory(categoryName);
    }
}
