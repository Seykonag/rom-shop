package kz.services.romshop.repositories;

import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.models.Product;
import kz.services.romshop.utilits.CalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        product.setStock(true);
        if (changed) save(product);
    }

    default List<Product> findProductsByCategory(Long id) {
        List<Product> allProducts = findAll();
        List<Product> categoryProducts = new ArrayList<>();

        for (Product product: allProducts) {
            if (Objects.equals(product.getCategories().getId(), id)) {
                categoryProducts.add(product);
            }
        }

        return  categoryProducts;
    }
}
