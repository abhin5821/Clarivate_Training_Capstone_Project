package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    @Query("SELECT MAX(t.capacity) FROM RestaurantTable t")
    Integer findMaxCapacity();

    boolean existsByTableNumber(Integer tableNumber);
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
}