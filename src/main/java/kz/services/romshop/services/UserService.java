package kz.services.romshop.services;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.Role;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

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

    public void delete(Long id) {
        repository.delete(repository.getReferenceById(id));
    }

    public void upgrade (RegistrationDTO dto, String username) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()
                && !dto.getPassword().equals(dto.getMatchingPassword())) throw new RuntimeException("Error password");

        if (repository.findByUsername(username).isPresent()) {
            updateProfile(dto, repository.findByUsername(username).get());
        } else throw new RuntimeException("Not found user");
    }

    private void updateProfile(RegistrationDTO dto, User savedUser) {
        boolean changed = false;

        if (savedUser == null) throw new RuntimeException("User not found by name " + dto.getUsername());


        Field[] dtoFields = dto.getClass().getDeclaredFields();
        Field[] savedFields = savedUser.getClass().getDeclaredFields();

        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            savedUser = repository.findByUsername(dto.getUsername()).get();
        }


        for (Field dtoField : dtoFields) {
            for (Field savedField : savedFields) {
                if (!savedField.getName().equals("username")) {
                    if (dtoField.getName().equals(savedField.getName())) {
                        dtoField.setAccessible(true);
                        savedField.setAccessible(true);

                        try {
                            Object dtoValue = dtoField.get(dto);
                            Object savedValue = savedField.get(savedUser);

                            if (dtoValue != null && !dtoValue.equals(savedValue)) {
                                savedField.set(savedUser, dtoValue);
                                changed = true;
                            }
                        } catch (IllegalAccessException exc) {
                            System.err.println(exc);
                        }
                    }
                }
            }
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            changed = true;
        }

        if (changed) save(savedUser);
    }

    public RegistrationDTO getProfile(String username) {
        User user = null;
        if (repository.findByUsername(username).isPresent()) {
            user = repository.findByUsername(username).get();
        }

        assert user != null;
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
