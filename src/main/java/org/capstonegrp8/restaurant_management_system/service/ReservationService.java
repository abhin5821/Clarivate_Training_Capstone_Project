package org.capstonegrp8.restaurant_management_system.service;



import org.capstonegrp8.restaurant_management_system.entity.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);

    List<Reservation> getAllReservations();

    Reservation getReservationById(Long id);

    Reservation updateReservation(Long id, Reservation reservation);

    void cancelReservation(Long id);
}