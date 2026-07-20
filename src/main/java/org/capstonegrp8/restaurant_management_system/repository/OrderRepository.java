package org.capstonegrp8.restaurant_management_system.repository;

import org.capstonegrp8.restaurant_management_system.enums.OrderStatus;
import org.capstonegrp8.restaurant_management_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders placed by a customer
    List<Order> findByCustomerCustomerId(Long customerId);

    // Get orders by status
    List<Order> findByOrderStatus(OrderStatus orderStatus);

    // Find orders with total amount greater than a value
    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

    // Find orders with total amount less than a value
    List<Order> findByTotalAmountLessThan(BigDecimal amount);


    List<Order> findByOrderDateAfter(LocalDateTime date);


    List<Order> findByOrderDateBefore(LocalDateTime date);


    List<Order> findByOrderByOrderDateAsc();

    List<Order> findByOrderByOrderDateDesc();


    List<Order> findByOrderByTotalAmountAsc();

    List<Order> findByOrderByTotalAmountDesc();
}
