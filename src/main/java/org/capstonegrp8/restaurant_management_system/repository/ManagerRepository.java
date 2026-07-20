package org.capstonegrp8.restaurant_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository<Manager> extends JpaRepository<Manager, Long> {
}
