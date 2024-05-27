package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.ProductDTO;
import kz.services.romshop.mappers.ProductMapper;
import kz.services.romshop.models.*;
import kz.services.romshop.repositories.CategoryRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.UserRepository;
import kz.services.romshop.utilits.CalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CalculateUtils calculateUtils;
    private final BucketService bucketService;
    private final MarkService markService;
    private final CategoryService categoryService;

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

    @Transactional
    public void createProduct(ProductDTO productDTO) {
        if (productDTO.getCategoryId() == null) throw new RuntimeException("У продукта нет категории");
        Category category = categoryRepository.getReferenceById(productDTO.getCategoryId());

        System.out.println(productDTO.getRealPhoto());
        Product product = Product.builder()
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .salePrice(
                        categoryService.isSaleCategory(productDTO.getCategoryId()) ?
                                calculateUtils.calculateSale(
                                        productDTO.getPrice(), category.getSale().getSale()
                                ) : null
                )
                .model(productDTO.getModel())
                .developer(productDTO.getDeveloper())
                .photo(Base64.getDecoder().decode(productDTO.getRealPhoto()))
                .stock(true)
                .categories(category)
                .build();

        repository.save(product);
    }

    @Transactional
    public void upgradeProduct(Long id, ProductDTO request) {
        if (repository.findById(id).isPresent()) {
            Product product = repository.getReferenceById(id);

            if (request.getPrice() != null && request.getPrice().compareTo(product.getPrice()) != 0
                    && product.getCategories().getSale() != null) {
                product.setSalePrice(calculateUtils.calculateSale(
                        request.getPrice(),
                        categoryRepository.getReferenceById(
                                        product.getCategories().getId())
                                .getSale().getSale()
                ));

                repository.save(product);
            }

            repository.updateProduct(request, product);

        } else throw new RuntimeException("Такого продукта не существует");
    }

    @Transactional
    public void deleteProduct(Long id) { repository.delete(repository.getReferenceById(id)); }

    public ProductDTO getProduct(Long id) {
        Product product = repository.getReferenceById(id);

        return ProductDTO.builder()
                .id(product.getId())
                .categoryId(product.getCategories().getId())
                .percentageSale(Optional.ofNullable(product.getCategories().getSale())
                        .map(Sale::getSale)
                        .orElse(0))
                .title(product.getTitle())
                .model(product.getModel())
                .developer(product.getDeveloper())
                .stock(product.isStock())
                .photo(product.getPhoto())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .build();
    }

    public List<ProductDTO> getAll() {
        List<ProductDTO> products = mapper.fromProductList(repository.findAll());

        for (ProductDTO dto: products) {
            Product product = repository.getReferenceById(dto.getId());
            dto.setCategoryId(product.getCategories().getId());

            if (product.getCategories().getSale() != null) {
                dto.setPercentageSale(product.getCategories().getSale().getSale());
            }
        }

        return products;
    }

    public List<ProductDTO> findByCategory(String categoryName) {
        List<Product> products = repository.findProductsByCategory(categoryName);
        List<ProductDTO> answer = new ArrayList<>();

        for (Product product: products) {
            answer.add(
                    ProductDTO.builder()
                            .categoryId(product.getCategories().getId())
                            .model(product.getModel())
                            .title(product.getTitle())
                            .salePrice(product.getSalePrice())
                            .developer(product.getDeveloper())
                            .photo(product.getPhoto())
                            .stock(product.isStock())
                            .percentageSale(product.getCategories().getSale().getSale())
                            .price(product.getPrice())
                            .build()
            );
        }

        return answer;
    }
}
