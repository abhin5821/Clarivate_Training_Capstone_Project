package org.capstonegrp8.restaurant_management_system.repository;




import org.capstonegrp8.restaurant_management_system.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}