package org.capstonegrp8.restaurant_management_system.security;

import lombok.RequiredArgsConstructor;
import org.capstonegrp8.restaurant_management_system.entity.Customer;
import org.capstonegrp8.restaurant_management_system.entity.Manager;
import org.capstonegrp8.restaurant_management_system.entity.Waiter;
import org.capstonegrp8.restaurant_management_system.repository.CustomerRepository;
import org.capstonegrp8.restaurant_management_system.repository.ManagerRepository;
import org.capstonegrp8.restaurant_management_system.repository.WaiterRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;
    private final WaiterRepository waiterRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Optional<Manager> manager =
                managerRepository.findByEmail(email);

        if (manager.isPresent()) {
            return User.builder()
                    .username(manager.get().getEmail())
                    .password(manager.get().getPassword())
                    .roles("MANAGER")
                    .build();
        }

        Optional<Waiter> waiter =
                waiterRepository.findByEmail(email);

        if (waiter.isPresent()) {
            return User.builder()
                    .username(waiter.get().getEmail())
                    .password(waiter.get().getPassword())
                    .roles("WAITER")
                    .build();
        }

        Optional<Customer> customer =
                customerRepository.findByEmail(email);

        if (customer.isPresent()) {
            return User.builder()
                    .username(customer.get().getEmail())
                    .password(customer.get().getPassword())
                    .roles("CUSTOMER")
                    .build();
        }

        throw new UsernameNotFoundException("User not found");
    }
}