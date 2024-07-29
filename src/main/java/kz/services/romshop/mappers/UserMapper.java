package kz.services.romshop.mappers;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public RegistrationDTO toDto(User user) {
        return RegistrationDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fax(user.getFax())
                .company(user.getCompany())
                .address(user.getAddress())
                .country(user.getCountry())
                .region(user.getRegion())
                .index(user.getIndex())
                .newsletter(user.isNewsletter())
                .build();
    }
}
