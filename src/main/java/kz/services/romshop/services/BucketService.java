package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.models.Bucket;
import kz.services.romshop.models.Product;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.BucketRepository;
import kz.services.romshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Bucket createBucket (User user, List<Long> productId) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsById(productId);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    public void addProducts(Bucket bucket, List<Long> productId) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsById(productId));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }

    public List<Product> myProducts(Long id) {
        if (bucketRepository.findById(id).isPresent()) {
            List<Product> one = (bucketRepository.findById(id).get().getProducts());
            return one.stream()
                    .map(product -> {
                        Product newProduct = new Product();
                        newProduct.setId(product.getId());
                        newProduct.setTitle(product.getTitle());
                        newProduct.setPrice(product.getPrice());
                        newProduct.setModel(product.getModel());
                        return newProduct;
                    })
                    .toList();
        } else throw new RuntimeException("Проблемки");
    }

    private List<Product> getCollectRefProductsById (List<Long> productId) {
        return productId.stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }
}
