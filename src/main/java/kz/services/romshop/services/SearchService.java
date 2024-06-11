package kz.services.romshop.services;

import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.models.Product;
import kz.services.romshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductService productService;
    private final ProductRepository productRepository;

    public List<ProductDTO> indexing(String request) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(request);
        return products.stream()
                .map(product -> productService.getProduct(product.getId()))
                .collect(Collectors.toList());
    }
}
