package org.capstonegrp8.restaurant_management_system.controller;


import org.capstonegrp8.restaurant_management_system.entity.Manager;
import org.capstonegrp8.restaurant_management_system.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<Manager> addManager(@RequestBody Manager manager) {
        return new ResponseEntity<>(managerService.addManager(manager), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable Long id,
                                                 @RequestBody Manager manager) {
        return ResponseEntity.ok(managerService.updateManager(id, manager));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable Long id) {

        managerService.deleteManager(id);

        return ResponseEntity.ok("Manager deleted successfully.");
    }
}