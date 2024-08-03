package kz.services.romshop.services;

import kz.services.romshop.dto.CategoryDTO;
import kz.services.romshop.models.Category;
import kz.services.romshop.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    public void createCategory() {
        CategoryDTO dto = CategoryDTO.builder()
                .title("TestCategory")
                .build();

        categoryService.createCategory(dto);

        Category category = categoryRepository.getReferenceByTitle("TestCategory");
        Assertions.assertEquals("TestCategory", category.getTitle());
    }
}
