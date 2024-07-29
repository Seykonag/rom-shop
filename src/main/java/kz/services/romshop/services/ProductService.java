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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentService commentService;
    private final CalculateUtils calculateUtils;
    private final BucketService bucketService;
    private final MarkService markService;
    private final CategoryService categoryService;

    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userRepository.getReferenceByUsername(username);

        Bucket bucket =user.getBucket();

        if (bucket == null) throw new RuntimeException("Not bucket");
        bucketService.addProducts(bucket, Collections.singletonList(productId));
    }

    @Transactional
    public void addToUserMark(Long productId, String username) {
        User user = userRepository.getReferenceByUsername(username);

        Mark mark =user.getMark();

        if (mark == null) throw new RuntimeException("Not bucket");
        markService.addProducts(mark, Collections.singletonList(productId));
    }

    @Transactional
    public void createProduct(ProductDTO productDTO) {
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
            Product product = repository.getReferenceById(id);
            updateProductsFields(product, request);
            repository.save(product);
    }

    @Transactional
    public Boolean deleteProduct(List<Long> ids) {
        try {
            for (Long id: ids) {
                repository.delete(repository.getReferenceById(id));
            }
        } catch (Exception exc) {
            return false;
        }

        return true;
    }

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
                .comments(commentService.findByProduct(product.getId()))
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

    public List<ProductDTO> findByCategory(Long id) {
        List<Product> products = repository.findByCategoriesId(id);
        List<ProductDTO> answer = new ArrayList<>();

        for (Product product: products) {
            BigDecimal salePrice = product.getSalePrice() != null ? product.getSalePrice() : BigDecimal.ZERO;
            int percentageSale = product.getCategories().getSale() != null ? product.getCategories().getSale().getSale() : 0;

            answer.add(
                    ProductDTO.builder()
                            .id(product.getId())
                            .categoryId(product.getCategories().getId())
                            .model(product.getModel())
                            .title(product.getTitle())
                            .salePrice(salePrice)
                            .developer(product.getDeveloper())
                            .photo(product.getPhoto())
                            .stock(product.isStock())
                            .percentageSale(percentageSale)
                            .price(product.getPrice())
                            .build()
            );

        }

        return answer;
    }

    private void updateProductsFields(Product product, ProductDTO request) {
        if (request.getPrice() != null && request.getPrice().compareTo(product.getPrice()) != 0
                && product.getCategories().getSale() != null) {
            product.setSalePrice(calculateUtils.calculateSale(
                    request.getPrice(),
                    categoryRepository.getReferenceById(
                                    product.getCategories().getId())
                            .getSale().getSale()
            ));
        }

        if (request.getRealPhoto() != null) {
            product.setPhoto(Base64.getDecoder().decode(request.getRealPhoto()));
        }

        updateProductDetails(product, request);
    }

    private void updateProductDetails(Product product, ProductDTO request) {
        boolean changed = false;

        Field[] dtoFields = request.getClass().getDeclaredFields();
        Field[] productFields = product.getClass().getDeclaredFields();

        for (Field dtoField : dtoFields) {
            for (Field productField : productFields) {
                if (dtoField.getName().equals(productField.getName())) {
                    dtoField.setAccessible(true);
                    productField.setAccessible(true);

                    try {
                        Object dtoValue = dtoField.get(request);
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
        product.setStock(true); //разобраться почему тру наличие при апдейте
    }
}
