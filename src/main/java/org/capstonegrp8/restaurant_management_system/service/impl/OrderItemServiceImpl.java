package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.MenuItem;
import org.capstonegrp8.restaurant_management_system.entity.OrderItem;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
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

        RestaurantOrder order = orderRepository.findById(
                        orderItem.getRestaurantOrder().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        MenuItem menuItem = menuItemRepository.findById(
                        orderItem.getMenuItem().getItemId())
                .orElseThrow(() -> new RuntimeException("Menu Item not found"));

        if (Boolean.FALSE.equals(menuItem.getAvailable())) {
            throw new RuntimeException("Menu Item is unavailable.");
        }

        orderItem.setRestaurantOrder(order);
        orderItem.setMenuItem(menuItem);

        // Calculate subtotal
        double subTotal = menuItem.getPrice() * orderItem.getQuantity();
        orderItem.setSubTotal(subTotal);

        OrderItem savedItem = orderItemRepository.save(orderItem);

        // Update order total
        double total = order.getTotalAmount() == null ? 0 : order.getTotalAmount();
        total += subTotal;

        order.setTotalAmount(total);
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
                .orElseThrow(() -> new RuntimeException("Order Item not found"));
    }

    @Override
    public OrderItem updateOrderItem(Long id, OrderItem orderItem) {

        OrderItem existing = getOrderItemById(id);

        RestaurantOrder order = existing.getRestaurantOrder();

        // Remove old subtotal from total
        double total = order.getTotalAmount() - existing.getSubTotal();

        // Update quantity
        existing.setQuantity(orderItem.getQuantity());

        // Calculate new subtotal
        double newSubTotal = existing.getMenuItem().getPrice() * existing.getQuantity();
        existing.setSubTotal(newSubTotal);

        // Add new subtotal to total
        order.setTotalAmount(total + newSubTotal);

        orderRepository.save(order);

        return orderItemRepository.save(existing);
    }

    @Override
    public void deleteOrderItem(Long id) {

        OrderItem item = getOrderItemById(id);

        RestaurantOrder order = item.getRestaurantOrder();

        // Reduce order total
        order.setTotalAmount(order.getTotalAmount() - item.getSubTotal());

        orderRepository.save(order);

        orderItemRepository.delete(item);
    }
}