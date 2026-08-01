package org.capstonegrp8.restaurant_management_system.service;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {

    RestaurantTable addTable(RestaurantTable table);

    List<RestaurantTable> getAllTables();

    RestaurantTable getTableById(Long id);

    RestaurantTable updateTable(Long id, RestaurantTable table);

    // Waiter frees a table (e.g. after payment) — sets it AVAILABLE and triggers re-allocation
    RestaurantTable releaseTable(Long id);

    void deleteTable(Long id);
}