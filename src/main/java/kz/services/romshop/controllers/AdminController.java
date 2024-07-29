package kz.services.romshop.controllers;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public List<RegistrationDTO> usersList() { return userService.getAll(); }
}
