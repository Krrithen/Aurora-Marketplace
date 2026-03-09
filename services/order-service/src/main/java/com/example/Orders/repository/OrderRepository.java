package com.example.orders.repository;

import com.example.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findTop1ByUserIdAndOrderStatusOrderByDateOfOrderDesc(String userId, String orderStatus);

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByUserIdOrderByDateOfOrderDesc(String userId);
}
