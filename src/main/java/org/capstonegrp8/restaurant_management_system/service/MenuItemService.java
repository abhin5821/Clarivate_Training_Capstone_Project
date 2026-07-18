package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.model.MenuItem;
import org.capstonegrp8.restaurant_management_system.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository repository;

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

    public List<MenuItem> getAllItems() {
        return repository.findAll();
    }

    public MenuItem getItem(Long id) {
        return repository.findById(id).orElse(null);
    }

    public MenuItem saveItem(MenuItem item) {
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }
}