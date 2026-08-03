package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {

    boolean existsByEmail(String email);
    Optional<Waiter> findByEmail(String email);
}