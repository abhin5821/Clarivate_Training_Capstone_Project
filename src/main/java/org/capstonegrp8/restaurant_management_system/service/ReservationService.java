package org.capstonegrp8.restaurant_management_system.service;



import java.util.List;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);
    Reservation getReservationById(Long id);
    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByCustomerId(Long customerId);
    void cancelReservation(Long id);

    // Called when a waiter frees a table — re-allocates it to the best-fit waiting reservation
    void reallocateFreedTable(Long tableId);

    // Called when a waiter releases a table — marks the reservation that was
    // seated there as FINISHED (its dining cycle is over)
    void finishReservationForTable(Long tableId);

}