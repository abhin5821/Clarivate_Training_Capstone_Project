package org.capstonegrp8.restaurant_management_system.service;



import org.capstonegrp8.restaurant_management_system.entity.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);

    List<Reservation> getAllReservations();

    Reservation getReservationById(Long id);

    void cancelReservation(Long id);

    // Called when a waiter frees a table — re-allocates it to the best-fit waiting reservation
    void reallocateFreedTable(Long tableId);

    // Called when a waiter releases a table — marks the reservation that was
    // seated there as FINISHED (its dining cycle is over)
    void finishReservationForTable(Long tableId);
}