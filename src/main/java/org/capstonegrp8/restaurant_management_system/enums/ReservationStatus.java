package org.capstonegrp8.restaurant_management_system.enums;

public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    // Reservation's dining cycle is over: table has been released after the
    // seated party's order was completed and paid. Terminal, historical
    // status — lets the frontend distinguish "past" reservations from active
    // PENDING/CONFIRMED ones without deleting any records.
    FINISHED
}