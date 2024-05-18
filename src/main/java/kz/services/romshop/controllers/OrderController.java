package kz.services.romshop.controllers;

import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.models.Order;
import kz.services.romshop.repositories.OrderDetailsRepository;
import kz.services.romshop.repositories.OrderRepository;
import kz.services.romshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/order"})
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public void createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
        orderService.createOrder(principal.getName(), orderDTO.getIdProducts());
    }

    @GetMapping("/closed/{id}")
    public void closedOrder(@PathVariable Long id) {
        orderService.closedOrder(id);
    }

    @GetMapping("/myOrders")
    public List<OrderDTO> myOrders(Principal principal) { return orderService.myOrders(principal.getName()); }
}
