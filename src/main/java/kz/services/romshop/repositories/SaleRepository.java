package kz.services.romshop.repositories;

import kz.services.romshop.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    default boolean findByCategoryId(Long categoryId) {
        List<Sale> sales = findAll();

        for (Sale sale: sales) {
            if (Objects.equals(sale.getCategory().getId(), categoryId)) return true;
        }
        return false;
    }
}
