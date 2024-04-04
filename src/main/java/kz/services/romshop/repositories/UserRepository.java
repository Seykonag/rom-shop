package kz.services.romshop.repositories;

import kz.services.romshop.dto.RegistrationDTO;
import kz.services.romshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    default void updateProfile(RegistrationDTO dto, User savedUser, PasswordEncoder passwordEncoder) {
        boolean changed = false;

        if (savedUser == null) throw new RuntimeException("User not found by name " + dto.getUsername());


        Field[] dtoFields = dto.getClass().getDeclaredFields();
        Field[] savedFields = savedUser.getClass().getDeclaredFields();

        if (findByUsername(dto.getUsername()).isPresent()) {
            savedUser = findByUsername(dto.getUsername()).get();
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
}
