package kz.services.romshop.controllers;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.JwtAuthDTO;
import kz.services.romshop.dto.LoginDTO;
import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authenticationService;

    @Transactional
    @PostMapping("/registration")
    public JwtAuthDTO registration(@RequestBody RegistrationDTO request) {
        return authenticationService.signUp(request, false);
    }

    @PostMapping("/login")
    public JwtAuthDTO login(@RequestBody LoginDTO request) { return authenticationService.signIn(request); }
}
