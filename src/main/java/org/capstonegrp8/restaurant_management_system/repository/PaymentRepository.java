package org.capstonegrp8.restaurant_management_system.repository;

import org.capstonegrp8.restaurant_management_system.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Spring parses these method names into SQL queries automatically.

    // Find the (single) payment for a given reservation.
    Optional<Payment> findByReservation_ReservationId(Long reservationId);

    // Find all payments made by a specific customer.
    List<Payment> findByCustomer_CustomerId(Long customerId);

    // Find all payments handled by a specific waiter.
    List<Payment> findByWaiter_WaiterId(Long waiterId);
}
