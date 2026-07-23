package org.capstonegrp8.restaurant_management_system.controller;

import org.capstonegrp8.restaurant_management_system.model.Payment;
import org.capstonegrp8.restaurant_management_system.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return service.getPayment(id);
    }

    @PostMapping
    public Payment save(@RequestBody Payment payment) {
        return service.savePayment(payment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePayment(id);
    }
}
