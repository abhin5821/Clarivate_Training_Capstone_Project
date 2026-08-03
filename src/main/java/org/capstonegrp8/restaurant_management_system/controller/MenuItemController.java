package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.entity.MenuItem;
import org.capstonegrp8.restaurant_management_system.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MenuItem> addMenuItem(@Valid @RequestBody MenuItem menuItem) {
        return new ResponseEntity<>(menuItemService.addMenuItem(menuItem), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER', 'CUSTOMER')")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'WAITER', 'CUSTOMER')")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @Valid @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItem));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {

        menuItemService.deleteMenuItem(id);

        return ResponseEntity.ok("Menu Item deleted successfully.");
    }
}