package com.example.orders.service;

import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;

import java.util.List;

public interface OrdersService {

    Order addOrdersDetails(Order order);
    List<Order> getAllOrdersDetails();
    Order addOrderProducts(List<OrderItem> orderItems, String userId);
    Order getOrdersDetails(String userId);
    Order getOrderById(String orderId);
    void sendEmail(String to, String subject, String body);
    List<Order> getOrderHistory(String userId);
}
