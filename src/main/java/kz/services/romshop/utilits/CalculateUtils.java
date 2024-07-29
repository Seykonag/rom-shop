package kz.services.romshop.utilits;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class CalculateUtils {
    public BigDecimal calculateSale(BigDecimal price, int sale) {
        return price.subtract(
                price.multiply(
                        new BigDecimal(sale).divide(new BigDecimal(100),
                        new MathContext(2, RoundingMode.HALF_UP))
                )
        );
    }


    public Double multiplyProduct(BigDecimal price, BigDecimal amount) {
        return price.multiply(amount).doubleValue();
    }
}
