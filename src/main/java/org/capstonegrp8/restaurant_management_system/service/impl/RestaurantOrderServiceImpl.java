package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.entity.Payment;
import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
import org.capstonegrp8.restaurant_management_system.enums.PaymentStatus;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;
import org.capstonegrp8.restaurant_management_system.exception.BadRequestException;
import org.capstonegrp8.restaurant_management_system.exception.ResourceNotFoundException;
import org.capstonegrp8.restaurant_management_system.repository.PaymentRepository;
import org.capstonegrp8.restaurant_management_system.repository.ReservationRepository;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantOrderRepository;
import org.capstonegrp8.restaurant_management_system.repository.WaiterRepository;
import org.capstonegrp8.restaurant_management_system.service.RestaurantOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantOrderServiceImpl implements RestaurantOrderService {

    private final RestaurantOrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final WaiterRepository waiterRepository;
    private final PaymentRepository paymentRepository;

    public RestaurantOrderServiceImpl(RestaurantOrderRepository orderRepository,
                                      ReservationRepository reservationRepository,
                                      WaiterRepository waiterRepository,
                                      PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
        this.waiterRepository = waiterRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
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
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setTotalAmount(0.0);

        RestaurantOrder savedOrder = orderRepository.save(order);

        // Auto-create the linked payment as PENDING the moment the order
        // opens. Its amount is kept in sync with the order's totalAmount by
        // OrderItemServiceImpl as items are added/updated/removed. paymentMethod
        // stays unset until the waiter finalizes it once the order is COMPLETED.
        Payment payment = Payment.builder()
                .amount(0.0)
                .status(PaymentStatus.PENDING)
                .restaurantOrder(savedOrder)
                .build();
        Payment savedPayment = paymentRepository.save(payment);

        savedOrder.setPayment(savedPayment);
        return orderRepository.save(savedOrder);
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

        // totalAmount is system-computed from order items — never accepted
        // from the client, so the payment/order amounts can't drift apart.
        if (order.getStatus() != null) {
            if (existing.getStatus() == OrderStatus.COMPLETED
                    && order.getStatus() == OrderStatus.IN_PROGRESS) {
                throw new BadRequestException("A completed order cannot be reverted to in-progress");
            }
            existing.setStatus(order.getStatus());
        }

        return orderRepository.save(existing);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.delete(getOrderById(id));
    }
}
