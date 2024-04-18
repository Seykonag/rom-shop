package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.SaleDTO;
import kz.services.romshop.models.Product;
import kz.services.romshop.models.Sale;
import kz.services.romshop.repositories.CategoryRepository;
import kz.services.romshop.repositories.ProductRepository;
import kz.services.romshop.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final SaleRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createSale(SaleDTO dto) {
        int checkData = parseData(dto.getCreated()).compareTo(parseData(dto.getEnded()));

        if (repository.findByCategoryId(dto.getCategoryId())) {
            throw new RuntimeException("Скидка на категорию уже существует");
        }

        if (dto.getSale() == 0) throw new RuntimeException("Скидка равна 0");

        if (checkData >= 0) throw new RuntimeException("Дата начала выше даты окончания");

        Sale sale = Sale.builder()
                .sale(dto.getSale())
                .created(parseData(dto.getCreated()))
                .ended(parseData(dto.getEnded()))
                .category(
                        categoryRepository.getReferenceById(dto.getCategoryId())
                )
                .build();

        repository.save(sale);
        categoryService.newSale(dto.getCategoryId(), sale);
    }

    @Transactional
    public void deleteSale(Long id) {
        Sale sale = repository.getReferenceById(id);
        Long categoryId = sale.getCategory().getId();

        List<Product> products = productRepository.findProductsByCategory(categoryId);
        for (Product product: products) product.setSalePrice(null);

        categoryRepository.getReferenceById(categoryId).setSale(null);
        repository.delete(sale);
    }

    @Scheduled(fixedRate = 300000)
    public void checkDataEndedSale() {
        LocalDateTime nowDate = LocalDateTime.now();

        List<Sale> sales = repository.findAll();

        for (Sale sale: sales) {
            int check = nowDate.compareTo(sale.getEnded());

            if (check >= 0) deleteSale(sale.getId());
        }
    }

    // Пример строки для парсинга "2024-04-15T10:15:30"
    private LocalDateTime parseData(String data) {
        if (data == null) throw new RuntimeException("Проблемы с датой");

        LocalDateTime localDateTime = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            localDateTime = LocalDateTime.parse(data, formatter);
        } catch (DateTimeParseException exc) {
            throw new RuntimeException("Не правильный формат даты и времени");
        }

        return localDateTime;
    }
}
