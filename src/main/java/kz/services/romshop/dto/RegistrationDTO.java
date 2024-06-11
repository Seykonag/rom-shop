package kz.services.romshop.dto;

import kz.services.romshop.models.Country;
import kz.services.romshop.models.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String fax;
    private String company;
    private String address;
    private String city;
    private String index;
    private Country country;
    private Region region;
    private String password;
    private String matchingPassword;
    private boolean newsletter;
}
