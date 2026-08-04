package org.capstonegrp8.restaurant_management_system.repository;


import org.capstonegrp8.restaurant_management_system.entity.Payment;
import org.capstonegrp8.restaurant_management_system.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByRestaurantOrder_Reservation_RestaurantTable_TableIdAndStatusNot(Long id, PaymentStatus paymentStatus);
}