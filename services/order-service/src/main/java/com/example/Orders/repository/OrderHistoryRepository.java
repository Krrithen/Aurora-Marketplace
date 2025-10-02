package com.example.Orders.repository;

import com.example.Orders.entities.OrderHistory;
import com.example.Orders.entities.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistory,String> {
}
