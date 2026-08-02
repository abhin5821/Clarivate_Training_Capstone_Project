package org.capstonegrp8.restaurant_management_system.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "waiters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Waiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waiterId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Column(unique = true)
    private String phone;

    @Email(message = "Invalid email")
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "manager_id")
//    @JsonIgnore
    private Manager manager;

    @OneToMany(mappedBy = "waiter")
    @JsonIgnore
    private List<RestaurantTable> restaurantTables = new ArrayList<>();

    @OneToMany(mappedBy = "waiter", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RestaurantOrder> orders = new ArrayList<>();

    @NotBlank(message = "Password is required")
    private String password;
}