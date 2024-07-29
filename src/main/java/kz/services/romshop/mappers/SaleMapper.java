package kz.services.romshop.mappers;

import kz.services.romshop.dto.SaleDTO;
import kz.services.romshop.models.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {
    public SaleDTO fromSale(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .categoryId(sale.getCategory().getId())
                .created(sale.getCreated().toString())
                .ended(sale.getEnded().toString())
                .sale(sale.getSale())
                .build();
    }
}
