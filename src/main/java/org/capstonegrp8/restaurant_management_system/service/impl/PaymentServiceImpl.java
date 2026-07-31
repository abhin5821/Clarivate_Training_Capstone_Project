package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.Payment;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.enums.PaymentStatus;
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

        RestaurantOrder order = orderRepository.findById(
                        payment.getRestaurantOrder().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getPayment() != null) {
            throw new RuntimeException("Payment already exists for this order.");
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
                .orElseThrow(() -> new RuntimeException("Payment not found"));
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

        Payment payment = getPaymentById(id);

        paymentRepository.delete(payment);
    }
}