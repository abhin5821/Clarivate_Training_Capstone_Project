package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.entity.Manager;

import java.util.List;

public interface ManagerService {

    Manager addManager(Manager manager);

    List<Manager> getAllManagers();

    Manager getManagerById(Long id);

    Manager updateManager(Long id, Manager manager);

    void deleteManager(Long id);
}