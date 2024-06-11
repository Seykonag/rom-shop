package kz.services.romshop.controllers;

import kz.services.romshop.dto.OrderDTO;
import kz.services.romshop.models.OrderStatus;
import kz.services.romshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderStatusesController {
    private final OrderService service;

    @GetMapping("/newStatus")
    public List<OrderDTO> newOrders() { return service.getOrderStatus(OrderStatus.NEW); }

    @PostMapping("/newStatus")
    public void confirmNewOrders(@RequestBody Map<Long, Boolean> confirms) {
        service.confirmOrder(confirms);
    }

    @GetMapping("/cancelStatus")
    public List<OrderDTO> cancelOrders() { return service.getOrderStatus(OrderStatus.CANCELED); }

    @GetMapping("/appStatus")
    public List<OrderDTO> approvedOrders() { return service.getOrderStatus(OrderStatus.APPROVED); }

    @GetMapping("/paidStatus")
    public List<OrderDTO> paidOrders() { return service.getOrderStatus(OrderStatus.PAID); }

    @GetMapping("/closedStatus")
    public List<OrderDTO> closedOrders() { return service.getOrderStatus(OrderStatus.CLOSED); }
}
