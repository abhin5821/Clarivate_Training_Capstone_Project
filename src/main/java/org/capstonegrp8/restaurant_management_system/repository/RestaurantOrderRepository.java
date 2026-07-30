package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Long> {

    boolean existsByOrderTimeAndStatus(LocalDateTime orderTime, OrderStatus status);
    Optional<RestaurantOrder> findByOrderTimeAndStatus(LocalDateTime orderTime, OrderStatus status);
}