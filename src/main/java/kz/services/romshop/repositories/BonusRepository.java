package kz.services.romshop.repositories;

import kz.services.romshop.models.BonusScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusRepository extends JpaRepository<BonusScore, Long> {
}
