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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CalculateUtils calculateUtils;
    private final ProductRepository productRepository;
    private final CategoryRepository repository;

    public ResponseEntity<Map<String, Object>> createCategory(CategoryDTO dto) {
        Category category = Category.builder()
                .title(dto.getTitle())
                .build();

        repository.save(category);

        dto.setId(category.getId());

        Map<String, Object> response = Map.of("data", dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public void updateCategory(Long id, CategoryDTO dto) {
        Category category = repository.getReferenceById(id);
        boolean changed = false;

        if (!dto.getTitle().equals(category.getTitle())) {
            category.setTitle(dto.getTitle());
            changed = true;
        }

        if (changed) repository.save(category);
    }

    @Transactional
    public Boolean deleteCategory(List<Long> ids) {
        try {
            for (Long id: ids) {
                repository.delete(repository.getReferenceById(id));
            }
        } catch (RuntimeException exc) {
            return false;
        }

        return true;
    }


    public boolean isSaleCategory(Long id) {
        return repository.getReferenceById(id).getSale() != null;
    }


    //Процентная скидка
    @Transactional
    public void newSale(Long id, Sale sale) {
        List<Product> products = productRepository.findByCategoriesId(id);
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

    public List<CategoryDTO> getAll() {
        List<Category> categories = repository.findAll();
        List<CategoryDTO> answer = new ArrayList<>();

        for (Category category: categories) {
            answer.add(new CategoryDTO(category.getId() , category.getTitle()));
        }

        return answer;
    }

    public CategoryDTO get(Long id) {
        Category category = repository.getReferenceById(id);
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
    }
}
