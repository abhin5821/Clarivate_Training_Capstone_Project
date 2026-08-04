package org.capstonegrp8.restaurant_management_system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationId(Long reservationId);

    boolean existsByCustomer_CustomerIdAndReservationDate(Long customerId, LocalDateTime reservationDate);
    Optional<Reservation> findByCustomer_CustomerIdAndReservationDate(Long customerId, LocalDateTime reservationDate);

    // Best waiting candidate for a freed table:
    // largest party that still fits (min seat-waste), earliest reservation wins ties (FCFS)
    Optional<Reservation> findFirstByStatusAndPartySizeLessThanEqualOrderByPartySizeDescReservationDateAsc(
            ReservationStatus status, Integer capacity);

    // The still-active (CONFIRMED) reservation currently seated at a table —
    // used to mark it FINISHED once the table is released.
    List<Reservation> findByRestaurantTable_TableIdAndStatus(Long tableId, ReservationStatus status);
    List<Reservation> findByCustomer_CustomerId(Long customerId);
}