package kz.services.romshop.repositories;

import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    default void updateCategory(CategoryDTO dto, Category category) {
        boolean changed = false;

        if (category == null) throw new RuntimeException("Такой категории не существует");

        if (findById(category.getId()).isPresent()) category = findById(category.getId()).get();

        if (!dto.getTitle().equals(category.getTitle())) {
            category.setTitle(dto.getTitle());
            changed = true;
        }

        if (changed) save(category);
    }
}
