package org.capstonegrp8.restaurant_management_system.service.impl;


import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.repository.WaiterRepository;
import org.capstonegrp8.restaurant_management_system.service.WaiterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;
    private final PasswordEncoder passwordEncoder;

    public WaiterServiceImpl(WaiterRepository waiterRepository, PasswordEncoder passwordEncoder) {
        this.waiterRepository = waiterRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public Waiter addWaiter(Waiter waiter) {
        waiter.setPassword(
                passwordEncoder.encode(waiter.getPassword())
        );
        return waiterRepository.save(waiter);
    }

    @Override
    public List<Waiter> getAllWaiters() {
        return waiterRepository.findAll();
    }

    @Override
    public Waiter getWaiterById(Long id) {
        return waiterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Waiter not found with id : " + id));
    }

    @Override
    public Waiter updateWaiter(Long id, Waiter waiter) {

        Waiter existingWaiter = getWaiterById(id);

        existingWaiter.setName(waiter.getName());
        existingWaiter.setPhone(waiter.getPhone());
        existingWaiter.setEmail(waiter.getEmail());
        existingWaiter.setManager(waiter.getManager());

        return waiterRepository.save(existingWaiter);
    }

    @Override
    public void deleteWaiter(Long id) {

        Waiter waiter = getWaiterById(id);

        waiterRepository.delete(waiter);
    }
}