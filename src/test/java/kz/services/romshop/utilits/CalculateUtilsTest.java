package kz.services.romshop.utilits;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CalculateUtilsTest {

    @InjectMocks
    private CalculateUtils calculateUtils;

    @Test
    public void calculateSaleOne() {
        BigDecimal answer = calculateUtils.calculateSale(new BigDecimal(1000), 25);
        Assertions.assertEquals(new BigDecimal("750.00"), answer);
    }

    @Test
    public void calculateSaleTwo() {
        try {
            calculateUtils.calculateSale(new BigDecimal(1000), 440);
        } catch (RuntimeException exc) {
            Assertions.assertEquals("Error sale number", exc.getMessage());
        }
    }

    @Test
    public void calculateSaleThree() {
        BigDecimal answer = calculateUtils.calculateSale(new BigDecimal(1000), 0);
        Assertions.assertEquals(new BigDecimal(1000), answer);
    }

    @Test
    public void calculateSaleFour() {
        BigDecimal answer = calculateUtils.calculateSale(new BigDecimal(0), 10);
        Assertions.assertEquals(new BigDecimal("0.0"), answer);
    }

    @Test
    public void calculateSaleFive() {
        try {
            calculateUtils.calculateSale(new BigDecimal(-1000), 10);
        } catch (RuntimeException exc) {
            Assertions.assertEquals("Error sale number", exc.getMessage());
        }
    }

    @Test
    public void multiplyProductOne() {
        double answer = calculateUtils.multiplyProduct(new BigDecimal(1000), new BigDecimal(3));
        Assertions.assertEquals(3000, answer);
    }

    @Test
    public void multiplyProductTwo() {
        double answer = calculateUtils.multiplyProduct(new BigDecimal(1000), new BigDecimal(0));
        Assertions.assertEquals(0, answer);
    }

    @Test
    public void multiplyProductThree() {
        double answer = calculateUtils.multiplyProduct(new BigDecimal(0), new BigDecimal(3));
        Assertions.assertEquals(0, answer);
    }

}
