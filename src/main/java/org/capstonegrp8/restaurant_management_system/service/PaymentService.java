package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPayment(Payment payment);

    List<Payment> getAllPayments();

    Payment getPaymentById(Long id);

    Payment updatePayment(Long id, Payment payment);

    void deletePayment(Long id);
}