package org.capstonegrp8.restaurant_management_system.service.impl;



import org.capstonegrp8.restaurant_management_system.entity.Customer;
import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.entity.RestaurantTable;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;
import org.capstonegrp8.restaurant_management_system.enums.TableStatus;
import org.capstonegrp8.restaurant_management_system.repository.CustomerRepository;
import org.capstonegrp8.restaurant_management_system.repository.ReservationRepository;
import org.capstonegrp8.restaurant_management_system.repository.RestaurantTableRepository;
import org.capstonegrp8.restaurant_management_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final CustomerRepository customerRepository;

    @Value("${restaurant.opening-hour:10}")
    private int openingHour;

    @Value("${restaurant.closing-hour:22}")
    private int closingHour;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantTableRepository tableRepository,
                                  CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {

        validateReservation(reservation);

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

    private void validateReservation(Reservation reservation) {

        // validate customer details
        if (reservation.getCustomer() == null) {
            throw new RuntimeException("Customer details are required");
        } else if (reservation.getCustomer().getCustomerId() == null) {
            throw new RuntimeException("Customer details are required");
        }
        Customer customer = customerRepository.findById(reservation.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        reservation.setCustomer(customer);

        // validate party size
        Integer partySize = reservation.getPartySize();
        if (partySize == null || partySize <= 0) {
            throw new RuntimeException("Party size must be greater than 0");
        }

        // validate reservation time
        LocalDateTime reservationDate = reservation.getReservationDate();
        if (reservationDate == null) {
            throw new RuntimeException("Reservation time is required");
        }
        if (reservationDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reservation time cannot be in the past");
        }

        // validate operating hours
        int hour = reservationDate.getHour();
        if (hour < openingHour || hour >= closingHour) {
            throw new RuntimeException(
                    "Reservation time must be within operating hours ("
                            + openingHour + ":00 - " + closingHour + ":00)");
        }

        // check maximum supported capacity
        Integer maxCapacity = tableRepository.findMaxCapacity();
        if (maxCapacity == null) {
            throw new RuntimeException("No tables are configured");
        }
        if (partySize > maxCapacity) {
            throw new RuntimeException(
                    "Party size exceeds maximum supported capacity of " + maxCapacity);
        }

        // prevent duplicate submission
        boolean duplicate = reservationRepository
                .existsByCustomer_CustomerIdAndReservationDate(customer.getCustomerId(), reservationDate);
        if (duplicate) {
            throw new RuntimeException("A reservation already exists for this customer at the same time");
        }
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
        existing.setPartySize(reservation.getPartySize());
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