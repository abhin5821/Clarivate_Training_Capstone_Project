package org.capstonegrp8.restaurant_management_system.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.capstonegrp8.restaurant_management_system.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // System-managed: mirrors the parent order's totalAmount as items are
    // added/removed. Not required on incoming requests — the service layer
    // controls this value, not the client.
    private Double amount;

    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // Only required when the waiter finalizes the payment (order COMPLETED);
    // stays null while the order is still IN_PROGRESS. Enforced in
    // PaymentServiceImpl rather than via bean validation, since this field is
    // legitimately absent for most of the payment's lifecycle.
    private String paymentMethod;

    // System-managed: set once, automatically, when the order is created.
    @OneToOne
    @JoinColumn(name = "order_id")
//    @JsonIgnore
    private RestaurantOrder restaurantOrder;
}