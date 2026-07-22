package org.capstonegrp8.restaurant_management_system.repository;

import org.capstonegrp8.restaurant_management_system.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

}