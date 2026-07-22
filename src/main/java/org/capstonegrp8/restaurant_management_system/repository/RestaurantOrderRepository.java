package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Long> {

}