package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.entity.OrderItem;
import org.capstonegrp8.restaurant_management_system.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItem> create(@Valid @RequestBody OrderItem orderItem) {
        return new ResponseEntity<>(
                orderItemService.createOrderItem(orderItem),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAll() {
        return ResponseEntity.ok(orderItemService.getAllOrderItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.getOrderItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> update(@PathVariable Long id,
                                            @Valid @RequestBody OrderItem orderItem) {

        return ResponseEntity.ok(
                orderItemService.updateOrderItem(id, orderItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.ok("Order Item deleted successfully.");
    }
}