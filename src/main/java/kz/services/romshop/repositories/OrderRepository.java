package kz.services.romshop.repositories;

import kz.services.romshop.models.Order;
import kz.services.romshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    default List<Order> findByUser(User user) {
        List<Order> allOrders = findAll();
        List<Order> list = new ArrayList<>();

        for (Order order: allOrders) {
            if (order.getUser().getUsername().equals(user.getUsername())) {
                list.add(order);
            }
        }

        return list;
    }
}
