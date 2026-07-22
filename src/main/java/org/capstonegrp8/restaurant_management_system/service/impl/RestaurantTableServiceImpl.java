package org.capstonegrp8.restaurant_management_system.service.impl;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.capstonegrp8.restaurant_management_system.enums.TableStatus;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantTableRepository;
import org.capstonegrp8.restaurant_management_system.service.RestaurantTableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository tableRepository;

    public RestaurantTableServiceImpl(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public RestaurantTable addTable(RestaurantTable table) {

        if (table.getCapacity() <= 0) {
            throw new RuntimeException("Table capacity must be greater than 0");
        }

        if (table.getStatus() == null) {
            table.setStatus(TableStatus.AVAILABLE);
        }

        return tableRepository.save(table);
    }

    @Override
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public RestaurantTable getTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id : " + id));
    }

    @Override
    public RestaurantTable updateTable(Long id, RestaurantTable table) {

        RestaurantTable existing = getTableById(id);

        existing.setTableNumber(table.getTableNumber());
        existing.setCapacity(table.getCapacity());
        existing.setStatus(table.getStatus());
        existing.setWaiter(table.getWaiter());

        return tableRepository.save(existing);
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.delete(getTableById(id));
    }
}