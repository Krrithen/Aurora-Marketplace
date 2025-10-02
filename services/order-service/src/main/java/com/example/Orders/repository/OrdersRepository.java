package com.example.Orders.repository;

import com.example.Orders.entities.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends MongoRepository<Orders,String> {
    public Orders findByUserId(String userId);
}
