package kz.services.romshop.repositories;

import kz.services.romshop.models.PaidOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaidOrderRepository extends JpaRepository<PaidOrder, Long> {
}
