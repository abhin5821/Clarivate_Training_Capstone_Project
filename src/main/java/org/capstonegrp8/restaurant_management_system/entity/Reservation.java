package org.capstonegrp8.restaurant_management_system.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.capstonegrp8.restaurant_management_system.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    private Integer partySize;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;


    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RestaurantOrder> orders = new ArrayList<>();
}