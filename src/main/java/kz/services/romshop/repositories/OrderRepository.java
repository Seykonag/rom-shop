package kz.services.romshop.repositories;

import kz.services.romshop.models.Order;
import kz.services.romshop.models.OrderStatus;
import kz.services.romshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);
}
