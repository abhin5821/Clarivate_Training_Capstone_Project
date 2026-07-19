package org.capstonegrp8.restaurant_management_system.repository;




import org.capstonegrp8.restaurant_management_system.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Get all items of an order
    List<OrderItem> findByOrderOrderId(Long orderId);

    // Get all order items of a product
    List<OrderItem> findByProductId(Long productId);

}
