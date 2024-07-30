package kz.services.romshop.mappers;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public RegistrationDTO toDto(User user) {
        return RegistrationDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .newsletter(user.isNewsletter())
                .build();
    }
}
