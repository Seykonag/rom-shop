package kz.services.romshop.services;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.Role;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public List<RegistrationDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private RegistrationDTO toDTO(User user) {
        return RegistrationDTO.builder()
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

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public Long getIdByUsername(String username) {
        if (repository.findByUsername(username).isPresent()) {
            return repository.findByUsername(username).get().getId();
        } else throw new RuntimeException("Проблемки");
    }


    public UserDetailsService userDetailsService() { return this::getByUsername; }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
