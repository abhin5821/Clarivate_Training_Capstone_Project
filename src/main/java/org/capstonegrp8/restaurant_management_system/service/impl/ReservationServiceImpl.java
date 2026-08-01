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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Reservation createReservation(Reservation reservation) {

        validateReservation(reservation);

        // No table chosen by client — system allocates automatically
        reservation.setRestaurantTable(null);
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation saved = reservationRepository.save(reservation);

        tryAllocate(saved);
        return saved;
    }

    /**
     * Finds the best available table for the given reservation (min seat-waste).
     * If a table is found, marks it RESERVED and the reservation CONFIRMED.
     * If no table fits, reservation stays PENDING.
     */
    private void tryAllocate(Reservation reservation) {
        List<RestaurantTable> candidates = tableRepository
                .findByStatusAndCapacityGreaterThanEqualOrderByCapacityAsc(
                        TableStatus.AVAILABLE, reservation.getPartySize());

        if (candidates.isEmpty()) {
            // No table available right now — reservation waits in PENDING queue
            return;
        }

        // First result = smallest capacity that still fits = minimum seat-waste
        RestaurantTable best = candidates.get(0);
        best.setStatus(TableStatus.RESERVED);
        tableRepository.save(best);

        reservation.setRestaurantTable(best);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }

    /**

     * After a table becomes free, allocate it to the best waiting reservation:
     * the largest party that still fits (minimum seat-waste), earliest first on ties (FCFS).
     */
    private void tryAllocateFreedTable(RestaurantTable freedTable) {
        if (freedTable == null || freedTable.getStatus() != TableStatus.AVAILABLE) {
            return;
        }

        reservationRepository
                .findFirstByStatusAndPartySizeLessThanEqualOrderByPartySizeDescReservationDateAsc(
                        ReservationStatus.PENDING, freedTable.getCapacity())
                .ifPresent(candidate -> {
                    freedTable.setStatus(TableStatus.RESERVED);
                    tableRepository.save(freedTable);

                    candidate.setRestaurantTable(freedTable);
                    candidate.setStatus(ReservationStatus.CONFIRMED);
                    reservationRepository.save(candidate);
                });
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
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);

        // Free the table if one was assigned (PENDING reservations have none)
        RestaurantTable table = reservation.getRestaurantTable();
        if (table != null) {
            table.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(table);
        }

        // Soft-cancel: keep the record for audit history, just mark it cancelled
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Give the freed table (if any) to the best-fit waiting reservation
        tryAllocateFreedTable(table);
    }

    @Override
    @Transactional
    public void reallocateFreedTable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        tryAllocateFreedTable(table);
    }
}