package org.capstonegrp8.restaurant_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @NotNull(message = "Quantity is required")
    @Positive
    private Integer quantity;

    @Positive
//    private Double price;
    private Double subTotal;

    @NotNull(message = "Order is required")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private RestaurantOrder restaurantOrder;

    @NotNull(message = "Menu item is required")
    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}