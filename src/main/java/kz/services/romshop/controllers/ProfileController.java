package kz.services.romshop.controllers;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public RegistrationDTO profileUser(Principal principal) {
        User user = null;
        if (principal == null) throw new RuntimeException("You not authorize");
        if (repository.findByUsername(principal.getName()).isPresent()) {
            user = repository.findByUsername(principal.getName()).get();
        }

        assert user != null;
        RegistrationDTO dto = RegistrationDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fax(user.getFax())
                .company(user.getCompany())
                .address(user.getAddress())
                .country(user.getCountry())
                .region(user.getRegion())
                .index(user.getIndex())
                .newsletter(user.isNewsletter())
                .build();
        return dto;
    }

    @PostMapping
    public String updateProfileUser(@RequestBody RegistrationDTO dto, Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованы");


        if (dto.getPassword() != null
            && !dto.getPassword().isEmpty()
            && !dto.getPassword().equals(dto.getMatchingPassword())) {
            return "Error";
        }

        repository.updateProfile(dto, repository.findByUsername(principal.getName()).get(), passwordEncoder);
        return "redirect:/products";
    }
}
