package kz.services.romshop.repositories;

import kz.services.romshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContainingIgnoreCase(String name);
    List<Product> findByCategoriesId(Long id);
}
