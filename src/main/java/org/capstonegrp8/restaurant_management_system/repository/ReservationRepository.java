package org.capstonegrp8.restaurant_management_system.repository;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByCustomer_CustomerIdAndReservationDate(Long customerId, LocalDateTime reservationDate);
    Optional<Reservation> findByCustomer_CustomerIdAndReservationDate(Long customerId, LocalDateTime reservationDate);

    // Best waiting candidate for a freed table:
    // largest party that still fits (min seat-waste), earliest reservation wins ties (FCFS)
    Optional<Reservation> findFirstByStatusAndPartySizeLessThanEqualOrderByPartySizeDescReservationDateAsc(
            ReservationStatus status, Integer capacity);
}