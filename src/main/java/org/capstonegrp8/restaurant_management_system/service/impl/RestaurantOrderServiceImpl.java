package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;
import org.capstonegrp8.restaurant_management_system.exception.BadRequestException;
import org.capstonegrp8.restaurant_management_system.exception.ResourceNotFoundException;
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
        if (order.getReservation() == null || order.getReservation().getReservationId() == null) {
            throw new BadRequestException("Reservation reference is required");
        }
        if (order.getWaiter() == null || order.getWaiter().getWaiterId() == null) {
            throw new BadRequestException("Waiter reference is required");
        }

        Reservation reservation = reservationRepository.findById(order.getReservation().getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + order.getReservation().getReservationId()));

        // Orders can only be placed once the reservation is confirmed and a table is allocated
        if (reservation.getStatus() != ReservationStatus.CONFIRMED
                || reservation.getRestaurantTable() == null) {
            throw new BadRequestException(
                    "Order cannot be placed until the reservation is confirmed and a table is allocated");
        }

        Waiter waiter = waiterRepository.findById(order.getWaiter().getWaiterId())
                .orElseThrow(() -> new ResourceNotFoundException("Waiter not found with id: " + order.getWaiter().getWaiterId()));

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
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
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
        orderRepository.delete(getOrderById(id));
    }
}
