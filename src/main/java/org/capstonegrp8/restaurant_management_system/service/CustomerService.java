package org.capstonegrp8.restaurant_management_system.service;


import org.capstonegrp8.restaurant_management_system.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer addCustomer(Customer customer);

    /**
     * Identify a walk-in customer by phone number. Returns the existing
     * record if the phone is already registered (refreshing name/city),
     * otherwise creates a new one. This backs the passwordless customer
     * identify flow used by /auth/customer.
     */
    Customer findOrCreateByPhone(String phone, String name, String city);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);
}