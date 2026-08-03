package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.capstonegrp8.restaurant_management_system.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    @Query("SELECT MAX(t.capacity) FROM RestaurantTable t")
    Integer findMaxCapacity();

    // Returns AVAILABLE tables that fit the party, cheapest capacity first (min seat-waste)
    List<RestaurantTable> findByStatusAndCapacityGreaterThanEqualOrderByCapacityAsc(
            TableStatus status, Integer minCapacity);

    boolean existsByTableNumber(Integer tableNumber);
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
}