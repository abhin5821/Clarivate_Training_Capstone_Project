package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.model.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
}