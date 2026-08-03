package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItem createOrderItem(OrderItem orderItem);

    List<OrderItem> getAllOrderItems();

    OrderItem getOrderItemById(Long id);

    OrderItem updateOrderItem(Long id, OrderItem orderItem);

    void deleteOrderItem(Long id);
}