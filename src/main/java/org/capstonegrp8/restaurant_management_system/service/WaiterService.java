package org.capstonegrp8.restaurant_management_system.service;


import org.capstonegrp8.restaurant_management_system.entity.Waiter;

import java.util.List;

public interface WaiterService {

    Waiter addWaiter(Waiter waiter);

    List<Waiter> getAllWaiters();

    Waiter getWaiterById(Long id);

    Waiter updateWaiter(Long id, Waiter waiter);

    void deleteWaiter(Long id);
}