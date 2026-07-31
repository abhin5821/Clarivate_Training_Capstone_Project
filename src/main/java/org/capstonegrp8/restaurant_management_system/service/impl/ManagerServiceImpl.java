package org.capstonegrp8.restaurant_management_system.service.impl;


import org.capstonegrp8.restaurant_management_system.entity.Manager;
import org.capstonegrp8.restaurant_management_system.repository.ManagerRepository;
import org.capstonegrp8.restaurant_management_system.service.ManagerService;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public ManagerServiceImpl(ManagerRepository managerRepository,
                              PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Manager addManager(Manager manager) {
        manager.setPassword(
                passwordEncoder.encode(manager.getPassword())
        );
        return managerRepository.save(manager);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getManagerById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id : " + id));
    }

    @Override
    public Manager updateManager(Long id, Manager manager) {

        Manager existingManager = getManagerById(id);

        existingManager.setName(manager.getName());
        existingManager.setPhone(manager.getPhone());
        existingManager.setEmail(manager.getEmail());

        return managerRepository.save(existingManager);
    }

    @Override
    public void deleteManager(Long id) {

        Manager manager = getManagerById(id);

        managerRepository.delete(manager);
    }
}