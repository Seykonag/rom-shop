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
public class CommentDTO {
    private String username;
    private String firstName;
    private String lastName;
    private Long idOrder;
    private Long idProduct;
    private Integer rating;
    private String text;
    private LocalDateTime data;
}
