package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.capstonegrp8.restaurant_management_system.service.RestaurantTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    public RestaurantTableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<RestaurantTable> addTable(@Valid @RequestBody RestaurantTable table) {
        return new ResponseEntity<>(tableService.addTable(table), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateTable(@PathVariable Long id,
                                                       @Valid @RequestBody RestaurantTable table) {
        return ResponseEntity.ok(tableService.updateTable(id, table));
    }

    @PutMapping("/{id}/release")
    public ResponseEntity<RestaurantTable> releaseTable(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.releaseTable(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.ok("Table deleted successfully");
    }
}