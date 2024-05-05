package kz.services.romshop.controllers;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final UserService service;

    @GetMapping
    public RegistrationDTO profileUser(Principal principal) {
        if (principal == null) throw new RuntimeException("You not authorize");

        return service.getProfile(principal.getName());
    }

    @PostMapping
    public void updateProfileUser(@RequestBody RegistrationDTO dto, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");

        service.upgrade(dto, principal.getName());
    }
}
