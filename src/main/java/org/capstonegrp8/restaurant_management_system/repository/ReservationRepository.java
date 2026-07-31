package org.capstonegrp8.restaurant_management_system.repository;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByCustomer_CustomerIdAndReservationDate(Long customerId, LocalDateTime reservationDate);
    Optional<Reservation> findByCustomer_CustomerIdAndReservationDate(Long customerId, LocalDateTime reservationDate);
}