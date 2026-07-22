package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
import org.capstonegrp8.restaurant_management_system.repository.ReservationRepository;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantOrderRepository;
import org.capstonegrp8.restaurant_management_system.repository.WaiterRepository;
import org.capstonegrp8.restaurant_management_system.service.RestaurantOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantOrderServiceImpl implements RestaurantOrderService {

    private final RestaurantOrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final WaiterRepository waiterRepository;

    public RestaurantOrderServiceImpl(RestaurantOrderRepository orderRepository,
                                      ReservationRepository reservationRepository,
                                      WaiterRepository waiterRepository) {
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
        this.waiterRepository = waiterRepository;
    }

    @Override
    public RestaurantOrder createOrder(RestaurantOrder order) {

        Reservation reservation = reservationRepository.findById(
                        order.getReservation().getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Waiter waiter = waiterRepository.findById(
                        order.getWaiter().getWaiterId())
                .orElseThrow(() -> new RuntimeException("Waiter not found"));

        order.setReservation(reservation);
        order.setWaiter(waiter);

        order.setOrderTime(LocalDateTime.now());

        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<RestaurantOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public RestaurantOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public RestaurantOrder updateOrder(Long id, RestaurantOrder order) {

        RestaurantOrder existing = getOrderById(id);

        existing.setStatus(order.getStatus());
        existing.setTotalAmount(order.getTotalAmount());

        return orderRepository.save(existing);
    }

    @Override
    public void deleteOrder(Long id) {

        RestaurantOrder order = getOrderById(id);

        orderRepository.delete(order);
    }
}