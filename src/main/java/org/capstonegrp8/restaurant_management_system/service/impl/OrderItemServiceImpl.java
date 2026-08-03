package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.MenuItem;
import org.capstonegrp8.restaurant_management_system.entity.OrderItem;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.exception.BadRequestException;
import org.capstonegrp8.restaurant_management_system.exception.ResourceNotFoundException;
import org.capstonegrp8.restaurant_management_system.repository.MenuItemRepository;
import org.capstonegrp8.restaurant_management_system.repository.OrderItemRepository;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantOrderRepository;
import org.capstonegrp8.restaurant_management_system.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final RestaurantOrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                RestaurantOrderRepository orderRepository,
                                MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        if (orderItem.getRestaurantOrder() == null || orderItem.getRestaurantOrder().getOrderId() == null) {
            throw new BadRequestException("Order reference is required");
        }
        if (orderItem.getMenuItem() == null || orderItem.getMenuItem().getItemId() == null) {
            throw new BadRequestException("Menu item reference is required");
        }

        RestaurantOrder order = orderRepository.findById(orderItem.getRestaurantOrder().getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderItem.getRestaurantOrder().getOrderId()));

        MenuItem menuItem = menuItemRepository.findById(orderItem.getMenuItem().getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + orderItem.getMenuItem().getItemId()));

        if (Boolean.FALSE.equals(menuItem.getAvailable())) {
            throw new BadRequestException("Menu item '" + menuItem.getName() + "' is currently unavailable");
        }

        orderItem.setRestaurantOrder(order);
        orderItem.setMenuItem(menuItem);

        double subTotal = menuItem.getPrice() * orderItem.getQuantity();
        orderItem.setSubTotal(subTotal);

        OrderItem savedItem = orderItemRepository.save(orderItem);

        double total = order.getTotalAmount() == null ? 0 : order.getTotalAmount();
        order.setTotalAmount(total + subTotal);
        orderRepository.save(order);

        return savedItem;
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));
    }

    @Override
    public OrderItem updateOrderItem(Long id, OrderItem orderItem) {
        OrderItem existing = getOrderItemById(id);
        RestaurantOrder order = existing.getRestaurantOrder();

        double total = order.getTotalAmount() == null ? 0 : order.getTotalAmount();
        total -= existing.getSubTotal();

        existing.setQuantity(orderItem.getQuantity());
        double newSubTotal = existing.getMenuItem().getPrice() * existing.getQuantity();
        existing.setSubTotal(newSubTotal);

        order.setTotalAmount(total + newSubTotal);
        orderRepository.save(order);

        return orderItemRepository.save(existing);
    }

    @Override
    public void deleteOrderItem(Long id) {
        OrderItem item = getOrderItemById(id);
        RestaurantOrder order = item.getRestaurantOrder();

        double total = order.getTotalAmount() == null ? 0 : order.getTotalAmount();
        order.setTotalAmount(total - item.getSubTotal());
        orderRepository.save(order);

        orderItemRepository.delete(item);
    }
}
