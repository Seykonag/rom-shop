package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.dto.JwtAuthDTO;
import kz.services.romshop.dto.LoginDTO;
import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.Country;
import kz.services.romshop.models.Region;
import kz.services.romshop.models.Role;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final BucketService bucketService;
    private final JwtService jwtService;
    private final MarkService markService;
    private final BonusService bonusService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public JwtAuthDTO signUp(RegistrationDTO registrationDTO, boolean admin) {
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(admin ? Role.ROLE_ADMIN : Role.ROLE_USER)
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .phone(registrationDTO.getPhone())
                .fax(registrationDTO.getFax())
                .company(registrationDTO.getCompany())
                .address(registrationDTO.getAddress())
                .city(registrationDTO.getCity())
                .index(registrationDTO.getIndex())
                .country(registrationDTO.getCountry())
                .region(registrationDTO.getRegion())
                .newsletter(registrationDTO.isNewsletter())
                .build();

        userService.create(user);
        bucketService.createBucket(user);
        markService.createMark(user);
        bonusService.createBonusScore(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthDTO(jwt);
    }

    public JwtAuthDTO signIn(LoginDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthDTO(jwt);
    }

    @Transactional
    public void firstAdmin() {
        if (repository.findByUsername("admin").isEmpty()) {
            User user = User.builder()
                    .username("admin")
                    .email("admin@email.com")
                    .firstName("admin")
                    .lastName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ROLE_ADMIN)
                    .phone("adminPhone")
                    .address("admin home 1")
                    .city("adminskoe")
                    .country(Country.KAZAKHSTAN)
                    .region(Region.PAVLODARSKIY)
                    .newsletter(false)
                    .build();

            userService.create(user);
            bucketService.createBucket(user);
            markService.createMark(user);
            bonusService.createBonusScore(user);
        }
    }
}
