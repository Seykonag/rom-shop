package kz.services.romshop.repositories;

import kz.services.romshop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getReferenceByTitle(String title);
}
