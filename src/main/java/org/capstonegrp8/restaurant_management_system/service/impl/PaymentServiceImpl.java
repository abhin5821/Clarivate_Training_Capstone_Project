package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.Payment;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.enums.PaymentStatus;
import org.capstonegrp8.restaurant_management_system.exception.BadRequestException;
import org.capstonegrp8.restaurant_management_system.exception.ConflictException;
import org.capstonegrp8.restaurant_management_system.exception.ResourceNotFoundException;
import org.capstonegrp8.restaurant_management_system.repository.PaymentRepository;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantOrderRepository;
import org.capstonegrp8.restaurant_management_system.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestaurantOrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              RestaurantOrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getRestaurantOrder() == null || payment.getRestaurantOrder().getOrderId() == null) {
            throw new BadRequestException("Order reference is required");
        }

        RestaurantOrder order = orderRepository.findById(payment.getRestaurantOrder().getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + payment.getRestaurantOrder().getOrderId()));

        if (order.getPayment() != null) {
            throw new ConflictException("Payment already exists for order with id: " + order.getOrderId());
        }

        payment.setRestaurantOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentTime(LocalDateTime.now());

        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PAID);
        }

        Payment savedPayment = paymentRepository.save(payment);
        order.setPayment(savedPayment);
        orderRepository.save(order);

        return savedPayment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public Payment updatePayment(Long id, Payment payment) {
        Payment existing = getPaymentById(id);
        existing.setPaymentMethod(payment.getPaymentMethod());
        if (payment.getStatus() != null) {
            existing.setStatus(payment.getStatus());
        }
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.delete(getPaymentById(id));
    }
}
