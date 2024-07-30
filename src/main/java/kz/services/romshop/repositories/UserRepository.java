package kz.services.romshop.repositories;

import kz.services.romshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getReferenceByEmail(String email);
    boolean existsByEmail(String email);
}
