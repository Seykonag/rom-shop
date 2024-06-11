package kz.services.romshop.controllers;

import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/{request}")
    public List<ProductDTO> searching(@PathVariable String request) {
        return searchService.indexing(request);
    }
}
