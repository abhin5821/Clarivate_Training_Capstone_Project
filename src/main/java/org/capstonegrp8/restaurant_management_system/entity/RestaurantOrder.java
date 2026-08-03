package org.capstonegrp8.restaurant_management_system.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalAmount;

    @NotNull(message = "Reservation is required")
    @ManyToOne
    @JoinColumn(name = "reservation_id")
//    @JsonIgnore
    private Reservation reservation;

    @NotNull(message = "Waiter is required")
    @ManyToOne
    @JoinColumn(name = "waiter_id")
//    @JsonIgnore
    private Waiter waiter;

    @OneToMany(mappedBy = "restaurantOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "restaurantOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    private Payment payment;
}

