package kz.services.romshop.controllers;

import kz.services.romshop.dto.BucketDTO;
import kz.services.romshop.services.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;


    @GetMapping
    public BucketDTO myBucket(Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");
        return bucketService.getBucketByUsername(principal.getName());
    }

    @PostMapping("/delete")
    public void deleteProduct(@RequestBody List<Long> id, Principal principal) {
        bucketService.deleteProduct(principal.getName(), id);
    }

    @GetMapping("/clear")
    public void cleat(Principal principal) {
        bucketService.clear(principal.getName());
    }
}
