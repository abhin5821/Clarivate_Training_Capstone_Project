package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;

import java.util.List;

public interface RestaurantOrderService {

    RestaurantOrder createOrder(RestaurantOrder order);

    List<RestaurantOrder> getAllOrders();

    RestaurantOrder getOrderById(Long id);

    RestaurantOrder updateOrder(Long id, RestaurantOrder order);

    void deleteOrder(Long id);
}