package kz.services.romshop.controllers;

import kz.services.romshop.models.Product;
import kz.services.romshop.services.BucketService;
import kz.services.romshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;
    private final UserService userService;


    @GetMapping
    public List<Product> myBucket(Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        Long id = userService.getIdByUsername(principal.getName());
        return bucketService.myProducts(id);
    }
}
