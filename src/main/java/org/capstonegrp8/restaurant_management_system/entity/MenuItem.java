package org.capstonegrp8.restaurant_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.capstonegrp8.restaurant_management_system.enums.MenuCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotBlank(message = "Item name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    @Positive(message = "Price must be positive")
    private Double price;

    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "manager_id")
//    @JsonIgnore
    private Manager manager;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();
}