package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.model.Waiter;
import org.capstonegrp8.restaurant_management_system.repository.WaiterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaiterService {

    private final WaiterRepository repository;

    public WaiterService(WaiterRepository repository) {
        this.repository = repository;
    }

    public List<Waiter> getAllWaiters() {
        return repository.findAll();
    }

    public Waiter getWaiter(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Waiter saveWaiter(Waiter waiter) {
        return repository.save(waiter);
    }

    public void deleteWaiter(Long id) {
        repository.deleteById(id);
    }
}