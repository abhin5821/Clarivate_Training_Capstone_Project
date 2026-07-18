package org.capstonegrp8.restaurant_management_system.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String name;

    private String category;

    private Double price;

    private Boolean availability;

    @ManyToOne
    @JoinColumn(name="manager_id")
    private Manager manager;
}