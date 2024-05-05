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
    private final ProductRepository repository;

    @GetMapping
    public List<ProductDTO> products() { return service.getAll(); }

    @GetMapping("/{id}")
    public Product productInfo(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        if (repository.findById(id).isPresent()) return repository.findById(id).get();
        else throw new RuntimeException("Такого продукта не существует");
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
}
