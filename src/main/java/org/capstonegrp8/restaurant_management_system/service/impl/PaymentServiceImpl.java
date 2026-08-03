package org.capstonegrp8.restaurant_management_system.service.impl;

import org.capstonegrp8.restaurant_management_system.entity.Payment;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantOrder;
import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
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

        // Normally unreachable in practice: a payment is auto-created the
        // moment an order is opened (RestaurantOrderServiceImpl.createOrder).
        // This guard only protects legacy/seeded orders that predate that
        // change and never got one.
        if (order.getPayment() != null) {
            throw new ConflictException("Payment already exists for order with id: " + order.getOrderId());
        }

        payment.setRestaurantOrder(order);
        payment.setAmount(order.getTotalAmount() == null ? 0.0 : order.getTotalAmount());
        payment.setPaymentTime(null);

        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PENDING);
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

    /**
     * The only real action performed here: finalizing a payment once its
     * order has been marked COMPLETED. Amount is never taken from the
     * request — it always mirrors order.totalAmount, kept in sync live by
     * OrderItemServiceImpl as items are added/updated/removed.
     */
    @Override
    public Payment updatePayment(Long id, Payment payment) {
        Payment existing = getPaymentById(id);
        RestaurantOrder order = existing.getRestaurantOrder();

        if (order == null || order.getStatus() != OrderStatus.COMPLETED) {
            throw new BadRequestException(
                    "Payment can only be finalized once the order is completed. Current status: "
                            + (order == null ? "UNKNOWN" : order.getStatus()));
        }

        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isBlank()) {
            throw new BadRequestException("Payment method is required to finalize payment");
        }

        existing.setPaymentMethod(payment.getPaymentMethod());
        existing.setStatus(payment.getStatus() != null ? payment.getStatus() : PaymentStatus.PAID);
        existing.setPaymentTime(LocalDateTime.now());

        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.delete(getPaymentById(id));
    }
}
