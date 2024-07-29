package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.BucketDetailsDTO;
import kz.services.romshop.dto.MarkDTO;
import kz.services.romshop.models.Mark;
import kz.services.romshop.models.Product;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.MarkRepository;
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
public class MarkService {
    private final UserService userService;
    private final MarkRepository markRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Mark createMark(User user) {
        Mark mark = Mark.builder()
                .id(user.getId())
                .user(user)
                .build();

        Mark savedMark = markRepository.save(mark);

        user.setMark(savedMark);
        userRepository.save(user);

        return savedMark;
    }

    public void addProducts(Mark mark, List<Long> productId) {
        List<Product> products = mark.getProducts();
        List<Product> newProducts = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProducts.addAll(getCollectRefProductsById(productId));
        mark.setProducts(newProducts);
        markRepository.save(mark);
    }

    public MarkDTO getMarkByUsername(String username) {
        User user = userService.getByUsername(username);

        MarkDTO markDTO = new MarkDTO();

        Map<Long, BucketDetailsDTO> mapByProductId = new HashMap<>();
        List<Product> products = user.getMark().getProducts();

        for (Product product: products) {
            BucketDetailsDTO detail = mapByProductId.get(product.getId());

            if (detail == null) mapByProductId.put(product.getId(), new BucketDetailsDTO(product));
            else {
                detail.setAmount(detail.getAmount().add(new BigDecimal("1.0")));
                detail.setSum(detail.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }

        markDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        markDTO.formation();
        return markDTO;
    }

    private List<Product> getCollectRefProductsById(List<Long> productId) {
        return productId.stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }
}
