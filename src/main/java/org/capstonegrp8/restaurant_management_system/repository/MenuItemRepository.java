package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}