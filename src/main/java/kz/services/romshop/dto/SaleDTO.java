package kz.services.romshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {
    private Long id;
    private Long categoryId;
    private String created;
    private String ended;
    private int sale;
}
