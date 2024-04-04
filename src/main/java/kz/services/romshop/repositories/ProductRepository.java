package kz.services.romshop.repositories;

import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    default void updateProduct(ProductDTO productDTO, Product product) {
        boolean changed = false;

        if (product == null) throw new RuntimeException("Такого продукта не существует");

        Field[] dtoFields = productDTO.getClass().getDeclaredFields();
        Field[] productFields = product.getClass().getDeclaredFields();

        if (findById(product.getId()).isPresent()) product = findById(product.getId()).get();

        for (Field dtoField : dtoFields) {
            for (Field productField : productFields) {
                if (dtoField.getName().equals(productField.getName())) {
                    dtoField.setAccessible(true);
                    productField.setAccessible(true);

                    try {
                        Object dtoValue = dtoField.get(productDTO);
                        Object savedValue = productField.get(product);

                        if (dtoValue != null && !dtoValue.equals(savedValue)) {
                            productField.set(product, dtoValue);
                            changed = true;
                        }
                    } catch (IllegalAccessException exc) {
                        throw new RuntimeException("Продукт не изменен");
                    }
                }
            }
        }

        if (changed) save(product);

    }
}
