package kz.services.romshop.controllers;

import kz.services.romshop.dto.CommentDTO;
import kz.services.romshop.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService service;

    @PostMapping("/create")
    public void createComment(@RequestBody CommentDTO request, Principal principal) {
        service.createComment(request, principal.getName());
    }

    @GetMapping("/findByProduct")
    public List<CommentDTO> findByProduct(@RequestParam("idProduct") Long id) {
        return service.findByProduct(id);
    }
}
