package kz.services.romshop.controllers;

import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/order"})
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    public List<OrderDTO> getAll(Principal principal) {
        if (principal == null) throw new RuntimeException("Не авторизованный админ");

        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public OrderDTO get(@PathVariable Long id) { return orderService.getOrderById(id); }

    @PostMapping("/create")
    public void createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
        orderService.createOrder(principal.getName(), orderDTO.getIdProducts());
    }

    @GetMapping("/closed/{id}")
    public void closedOrder(@PathVariable Long id) {
        orderService.closedOrder(id);
    }

    @PostMapping("/cancel")
    public void confirmNewOrders(@RequestBody Map<Long, Boolean> confirms, Principal principal) {
        orderService.confirmOrder(confirms, principal.getName());
    }

    @GetMapping("/myOrders")
    public List<OrderDTO> myOrders(Principal principal) { return orderService.myOrders(principal.getName()); }
}
