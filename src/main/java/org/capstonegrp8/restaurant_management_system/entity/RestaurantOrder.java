package org.capstonegrp8.restaurant_management_system.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "reservation_id")
//    @JsonIgnore
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "waiter_id")
//    @JsonIgnore
    private Waiter waiter;

    @OneToMany(mappedBy = "restaurantOrder", cascade = CascadeType.ALL)
//    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "restaurantOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    private Payment payment;
}





//package org.capstonegrp8.restaurant_management_system.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.*;
//import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "restaurant_orders")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RestaurantOrder {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long orderId;
//
//    private LocalDateTime orderTime;
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
//
//    private Double totalAmount;
//
//    @ManyToOne
//    @JoinColumn(name = "reservation_id")
//    @JsonBackReference(value = "reservation-order")
//    private Reservation reservation;
//
//    @ManyToOne
//    @JoinColumn(name = "waiter_id")
//    private Waiter waiter;
//
//    @OneToMany(mappedBy = "restaurantOrder", cascade = CascadeType.ALL)
//    @JsonBackReference(value = "reservation-order")
//    private List<OrderItem> orderItems = new ArrayList<>();
//
//    @OneToOne(mappedBy = "restaurantOrder", cascade = CascadeType.ALL)
//    @JsonManagedReference(value = "order-payment")
//    private Payment payment;
//}