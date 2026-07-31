package org.capstonegrp8.restaurant_management_system.service.impl;


import org.capstonegrp8.restaurant_management_system.entity.MenuItem;
import org.capstonegrp8.restaurant_management_system.repository.MenuItemRepository;
import org.capstonegrp8.restaurant_management_system.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItem addMenuItem(MenuItem menuItem) {

        if(menuItem.getPrice() <= 0){
            throw new RuntimeException("Price should be greater than zero.");
        }

        return menuItemRepository.save(menuItem);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu Item not found with id : " + id));
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem menuItem) {

        MenuItem existing = getMenuItemById(id);

        existing.setName(menuItem.getName());
        existing.setCategory(menuItem.getCategory());
        existing.setPrice(menuItem.getPrice());
        existing.setAvailable(menuItem.getAvailable());
        existing.setManager(menuItem.getManager());

        return menuItemRepository.save(existing);
    }

    @Override
    public void deleteMenuItem(Long id) {

        MenuItem menuItem = getMenuItemById(id);

        menuItemRepository.delete(menuItem);
    }
}