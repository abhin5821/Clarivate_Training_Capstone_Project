package org.capstonegrp8.restaurant_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "party_size", nullable = false)
    private int partySize;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;


    //the relationship field
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;
}