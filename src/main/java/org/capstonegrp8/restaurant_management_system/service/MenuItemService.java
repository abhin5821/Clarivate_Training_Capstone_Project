package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.entity.MenuItem;

import java.util.List;

public interface MenuItemService {

    MenuItem addMenuItem(MenuItem menuItem);

    List<MenuItem> getAllMenuItems();

    MenuItem getMenuItemById(Long id);

    MenuItem updateMenuItem(Long id, MenuItem menuItem);

    void deleteMenuItem(Long id);
}