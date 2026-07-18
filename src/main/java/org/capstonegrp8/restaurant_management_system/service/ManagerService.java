package org.capstonegrp8.restaurant_management_system.service;


import org.capstonegrp8.restaurant_management_system.model.Manager;
import org.capstonegrp8.restaurant_management_system.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerRepository repository;

    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    public List<Manager> getAllManagers() {
        return repository.findAll();
    }

    public Manager getManager(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Manager saveManager(Manager manager) {
        return repository.save(manager);
    }

    public void deleteManager(Long id) {
        repository.deleteById(id);
    }
}