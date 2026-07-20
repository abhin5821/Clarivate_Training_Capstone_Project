package org.capstonegrp8.restaurant_management_system.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Waiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waiterId;

    private String name;

    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(name="manager_id")
    private Manager manager;
}