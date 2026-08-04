package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.service.WaiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/waiters")
public class WaiterController {

    private final WaiterService waiterService;

    public WaiterController(WaiterService waiterService) {
        this.waiterService = waiterService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Waiter> addWaiter(@Valid @RequestBody Waiter waiter) {
        return new ResponseEntity<>(waiterService.addWaiter(waiter), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER')")
    public ResponseEntity<List<Waiter>> getAllWaiters() {
        return ResponseEntity.ok(waiterService.getAllWaiters());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER')")
    public ResponseEntity<Waiter> getWaiterById(@PathVariable Long id) {
        return ResponseEntity.ok(waiterService.getWaiterById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Waiter> updateWaiter(@PathVariable Long id,
                                               @Valid @RequestBody Waiter waiter) {
        return ResponseEntity.ok(waiterService.updateWaiter(id, waiter));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteWaiter(@PathVariable Long id) {

        waiterService.deleteWaiter(id);

        return ResponseEntity.ok("Waiter deleted successfully.");
    }
}