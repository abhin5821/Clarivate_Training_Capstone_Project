package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.service.RestaurantOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class RestaurantOrderController {

    private final RestaurantOrderService orderService;

    public RestaurantOrderController(RestaurantOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER')")
    public ResponseEntity<RestaurantOrder> createOrder(@Valid @RequestBody RestaurantOrder order) {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER')")
    public ResponseEntity<List<RestaurantOrder>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER', 'CUSTOMER')")
    public ResponseEntity<RestaurantOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // No @Valid here: updating an order is now a status-only transition
    // (IN_PROGRESS -> COMPLETED), so reservation/waiter are not expected
    // in the request body.
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER')")
    public ResponseEntity<RestaurantOrder> updateOrder(@PathVariable Long id,
                                                       @RequestBody RestaurantOrder order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER','WAITER')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {

        orderService.deleteOrder(id);

        return ResponseEntity.ok("Order deleted successfully.");
    }
}