package org.capstonegrp8.restaurant_management_system.service.impl;



import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;
import org.capstonegrp8.restaurant_management_system.enums.TableStatus;
import org.capstonegrp8.restaurant_management_system.repository.ReservationRepository;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantTableRepository;
import org.capstonegrp8.restaurant_management_system.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantTableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {

        System.out.println("Customer = " + reservation.getCustomer());
        System.out.println("Table = " + reservation.getRestaurantTable());

        RestaurantTable table = tableRepository.findById(
                        reservation.getRestaurantTable().getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Table is not available");
        }

        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);

        reservation.setRestaurantTable(table);

        if (reservation.getStatus() == null) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Override
    public Reservation updateReservation(Long id, Reservation reservation) {

        Reservation existing = getReservationById(id);

        existing.setReservationDate(reservation.getReservationDate());
        existing.setNumberOfGuests(reservation.getNumberOfGuests());
        existing.setStatus(reservation.getStatus());

        return reservationRepository.save(existing);
    }

    @Override
    public void cancelReservation(Long id) {

        Reservation reservation = getReservationById(id);

        RestaurantTable table = reservation.getRestaurantTable();
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);

        reservationRepository.delete(reservation);
    }
}