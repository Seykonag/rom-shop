package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.models.Category;
import kz.services.romshop.models.Product;
import kz.services.romshop.models.Sale;
import kz.services.romshop.repositories.CategoryRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.utilits.CalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CalculateUtils calculateUtils;
    private final ProductRepository productRepository;
    private final CategoryRepository repository;

    public void createCategory(CategoryDTO dto) {
        Category category = Category.builder()
                .title(dto.getTitle())
                .build();

        repository.save(category);
    }

    public void updateCategory(Long id, CategoryDTO dto) {
        repository.updateCategory(dto, repository.getReferenceById(id));
    }

    @Transactional
    public void deleteCategory(Long id) { repository.delete(repository.getReferenceById(id)); }


    public boolean isSaleCategory(Long id) {
        return repository.getReferenceById(id).getSale() != null;
    }


    //Процентная скидка
    @Transactional
    public void newSale(Long id, Sale sale) {
        List<Product> products = productRepository.findProductsByCategory(id);
        Category category = repository.getReferenceById(id);

        for (Product product: products) {
            BigDecimal price = product.getPrice();
            BigDecimal salePrice = calculateUtils.calculateSale(price, sale.getSale());
            product.setSalePrice(salePrice);
            productRepository.save(product);
        }

        category.setSale(sale);
        repository.save(category);
    }
}
