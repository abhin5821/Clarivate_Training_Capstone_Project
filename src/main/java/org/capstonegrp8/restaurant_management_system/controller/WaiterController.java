package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.service.WaiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiters")
public class WaiterController {

    private final WaiterService waiterService;

    public WaiterController(WaiterService waiterService) {
        this.waiterService = waiterService;
    }

    @PostMapping
    public ResponseEntity<Waiter> addWaiter(@RequestBody Waiter waiter) {
        return new ResponseEntity<>(waiterService.addWaiter(waiter), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Waiter>> getAllWaiters() {
        return ResponseEntity.ok(waiterService.getAllWaiters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Waiter> getWaiterById(@PathVariable Long id) {
        return ResponseEntity.ok(waiterService.getWaiterById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Waiter> updateWaiter(@PathVariable Long id,
                                               @RequestBody Waiter waiter) {
        return ResponseEntity.ok(waiterService.updateWaiter(id, waiter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWaiter(@PathVariable Long id) {

        waiterService.deleteWaiter(id);

        return ResponseEntity.ok("Waiter deleted successfully.");
    }
}