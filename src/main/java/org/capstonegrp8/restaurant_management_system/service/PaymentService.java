package org.capstonegrp8.restaurant_management_system.service;

import org.capstonegrp8.restaurant_management_system.enums.PaymentStatus;
import org.capstonegrp8.restaurant_management_system.model.Payment;
import org.capstonegrp8.restaurant_management_system.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    // Constructor injection: Spring passes in the repository bean automatically.
    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<Payment> getAllPayments() {
        return repository.findAll();
    }

    public Payment getPayment(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Payment savePayment(Payment payment) {
        // Small business rules applied here (not in the controller).
        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus(PaymentStatus.PENDING);
        }
        if (payment.getPaymentTime() == null) {
            payment.setPaymentTime(LocalDateTime.now());
        }
        return repository.save(payment);
    }

    public void deletePayment(Long id) {
        repository.deleteById(id);
    }
}
