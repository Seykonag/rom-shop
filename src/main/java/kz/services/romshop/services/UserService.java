package kz.services.romshop.services;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.mappers.UserMapper;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        return repository.save(user);
    }

    public List<RegistrationDTO> getAll() {
        return repository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public User create(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public User getByUsername(String email) { return repository.getReferenceByEmail(email); }

    public Boolean delete(List<Long> ids) {
        try {
            for (Long id: ids) repository.delete(repository.getReferenceById(id));
        } catch (Exception exc) {
            return false;
        }

        return true;
    }

    public void updateProfile(RegistrationDTO dto, String email) {
        User savedUser = repository.getReferenceByEmail(email);
        boolean changed = false;

        Field[] dtoFields = dto.getClass().getDeclaredFields();
        Field[] savedFields = savedUser.getClass().getDeclaredFields();

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

    public RegistrationDTO getProfile(String email) {
        return userMapper.toDto(
                repository.getReferenceByEmail(email)
        );
    }

    public UserDetailsService userDetailsService() { return this::getByUsername; }
}
