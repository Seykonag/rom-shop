package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.mappers.ProductMapper;
import kz.services.romshop.models.Bucket;
import kz.services.romshop.models.Mark;
import kz.services.romshop.models.Product;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final BucketService bucketService;
    private final MarkService markService;

    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user;

        if (userRepository.findByUsername(username).isPresent()) {
            user = userRepository.findByUsername(username).get();
        } else throw new RuntimeException("Пользователь не найден");

        Bucket bucket =user.getBucket();

        if (bucket == null) throw new RuntimeException("Not bucket");
        bucketService.addProducts(bucket, Collections.singletonList(productId));
    }

    @Transactional
    public void addToUserMark(Long productId, String username) {
        User user;

        if (userRepository.findByUsername(username).isPresent()) {
            user = userRepository.findByUsername(username).get();
        } else throw new RuntimeException("Пользователь не найден");

        Mark mark =user.getMark();

        if (mark == null) throw new RuntimeException("Not bucket");
        markService.addProducts(mark, Collections.singletonList(productId));
    }

    public void createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .model(productDTO.getModel())
                .developer(productDTO.getDeveloper())
                .photo(productDTO.getPhoto())
                .stock(true)
                .build();

        repository.save(product);
    }

    public List<ProductDTO> getAll() { return mapper.fromProductList(repository.findAll()); }
}
