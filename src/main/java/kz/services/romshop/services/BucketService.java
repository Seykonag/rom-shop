package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.BucketDTO;
import kz.services.romshop.dto.BucketDetailsDTO;
import kz.services.romshop.models.Bucket;
import kz.services.romshop.models.Product;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.BucketRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public Bucket createBucket(User user) {
        Bucket bucket = new Bucket();
        bucket.setId(user.getId()); // Присваиваем id Bucket таким же, как у пользователя
        bucket.setUser(user);

        // Сохраняем Bucket перед сохранением User, чтобы избежать проблем с целостностью данных
        Bucket savedBucket = bucketRepository.save(bucket);

        // Присваиваем сохраненный Bucket пользователю
        user.setBucket(savedBucket);
        userRepository.save(user);

        return savedBucket;
    }


    public void addProducts(Bucket bucket, List<Long> productId) {
        List<Product> products = bucket.getProducts();
        List<Product> newProducts = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProducts.addAll(getCollectRefProductsById(productId));
        bucket.setProducts(newProducts);
        bucketRepository.save(bucket);
    }

    @Transactional
    public BucketDTO getBucketByUsername(String name) {
        User user = userService.getByUsername(name);

        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailsDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product: products) {
            BucketDetailsDTO detail = mapByProductId.get(product.getId());

            if (product.getSalePrice() == null) {
                if (detail == null) mapByProductId.put(product.getId(), new BucketDetailsDTO(product));
                else {
                    detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                    detail.setSum(detail.getSum() + Double.valueOf(product.getPrice().toString()));
                }
            } else {
                if (detail == null) mapByProductId.put(product.getId(), new BucketDetailsDTO(product));
                else {
                    detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                    detail.setSum(detail.getSum() + Double.valueOf(product.getSalePrice().toString()));
                }
            }
        }


        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }

    public void deleteProduct(String username, List<Long> ids) {
        Bucket bucket = bucketRepository.getReferenceByUserUsername(username);
        List<Product> products = bucket.getProducts();
        List<Product> deleteProducts = new ArrayList<>();

        for (Long id: ids) deleteProducts.add(productRepository.getReferenceById(id));

        products.removeAll(deleteProducts);

        bucketRepository.save(bucket);
    }

    public void clear(String username) {
        Bucket bucket = bucketRepository.getReferenceByUserUsername(username);
        bucket.setProducts(null);

        bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsById(List<Long> productId) {
        return productId.stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }
}
