package org.capstonegrp8.restaurant_management_system.service.impl;


import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.capstonegrp8.restaurant_management_system.enums.TableStatus;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantTableRepository;
import org.capstonegrp8.restaurant_management_system.service.ReservationService;
import org.capstonegrp8.restaurant_management_system.service.RestaurantTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository tableRepository;
    private final ReservationService reservationService;

    public RestaurantTableServiceImpl(RestaurantTableRepository tableRepository,
                                      ReservationService reservationService) {
        this.tableRepository = tableRepository;
        this.reservationService = reservationService;
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
    @Transactional
    public RestaurantTable releaseTable(Long id) {

        RestaurantTable table = getTableById(id);

        table.setStatus(TableStatus.AVAILABLE);
        RestaurantTable saved = tableRepository.save(table);

        // Table just freed — hand it to the best-fit waiting reservation (min-waste + FCFS)
        reservationService.reallocateFreedTable(saved.getTableId());

        return getTableById(id);
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.delete(getTableById(id));
    }
}