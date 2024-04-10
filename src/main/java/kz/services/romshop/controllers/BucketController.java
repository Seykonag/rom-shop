package kz.services.romshop.controllers;

import kz.services.romshop.dto.BucketDTO;
import kz.services.romshop.services.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;


    @GetMapping
    public BucketDTO myBucket(Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");
        return bucketService.getBucketById(principal.getName());
    }
}
