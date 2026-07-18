package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.model.Manager;
import org.capstonegrp8.restaurant_management_system.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService service;

    public ManagerController(ManagerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Manager> getAll() {
        return service.getAllManagers();
    }

    @GetMapping("/{id}")
    public Manager getById(@PathVariable Long id) {
        return service.getManager(id);
    }

    @PostMapping
    public Manager save(@RequestBody Manager manager) {
        return service.saveManager(manager);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteManager(id);
    }
}