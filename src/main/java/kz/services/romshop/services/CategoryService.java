package kz.services.romshop.services;

import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.models.Category;
import kz.services.romshop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public void createCategory(CategoryDTO dto) {
        Category category = Category.builder()
                .title(dto.getTitle())
                .build();

        repository.save(category);
    }
}
