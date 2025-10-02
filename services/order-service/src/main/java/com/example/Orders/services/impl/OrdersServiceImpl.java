package com.example.Orders.services.impl;

import com.example.Orders.dto.OrderProductsDTO;
import com.example.Orders.dto.OrdersDTO;
import com.example.Orders.entities.OrderHistory;
import com.example.Orders.entities.Orders;
import com.example.Orders.repository.OrderHistoryRepository;
import com.example.Orders.repository.OrdersRepository;
import com.example.Orders.services.OrdersService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@ToString
@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Orders addOrdersDetails(Orders order) {
        log.info("Adding order details for user: {}", order.getUserId());
        
        Orders temp = ordersRepository.findByUserId(order.getUserId());
        if (temp == null) {
            log.error("Order not found for user: {}", order.getUserId());
            throw new IllegalArgumentException("Order not found for user: " + order.getUserId());
        }
        
        temp.setProductCount(temp.getProductsDTOList().size());
        temp.setTotalPrice(order.getTotalPrice());
        temp.setOrderStatus(order.getOrderStatus());
        temp.setModeOfPayment(order.getModeOfPayment());
        temp.setShippingAddress(order.getShippingAddress());
        
        log.info("Updated order details for user: {}, status: {}, total: {}", 
                order.getUserId(), order.getOrderStatus(), order.getTotalPrice());
        
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setUserId(temp.getUserId());
        orderHistory.setOrderId(temp.getUserId());
        
        List<Orders> tempList = new ArrayList<>();
        Optional<OrderHistory> existingHistory = orderHistoryRepository.findById(temp.getOrderId());
        
        if (existingHistory.isPresent()) {
            tempList = existingHistory.get().getOrders();
            log.info("Found existing order history for user: {}", temp.getUserId());
        } else {
            orderHistoryRepository.save(orderHistory);
            log.info("Created new order history for user: {}", temp.getUserId());
        }
        
        tempList.add(temp);
        orderHistory.setOrders(tempList);
        orderHistoryRepository.save(orderHistory);

        Orders savedOrder = ordersRepository.save(temp);
        log.info("Successfully saved order details for user: {}", order.getUserId());
        return savedOrder;
    }

    @Override
    public List<Orders> getAllOrdersDetails() {
        log.info("Retrieving all order details");
        List<Orders> orders = ordersRepository.findAll();
        log.info("Retrieved {} orders", orders.size());
        return orders;
    }


    @Override
    public Orders addOrderProducts(List<OrderProductsDTO> orderProductsDTOS, String userId) {
        log.info("Adding order products for user: {}, products count: {}", userId, orderProductsDTOS.size());
        
        Orders orders = new Orders();
        orders.setProductsDTOList(orderProductsDTOS);
        orders.setUserId(userId);
        orders.setOrderId(userId);

        orders.setOrderStatus("Payment Pending");
        orders.setDateOfOrder(LocalDateTime.now());
        orders.setShippingAddress("");
        orders.setTotalPrice(0.0);
        orders.setModeOfPayment("");
        
        Orders savedOrder = ordersRepository.save(orders);
        log.info("Successfully added order products for user: {}", userId);
        return savedOrder;
    }

    @Override
    public Orders getOrdersDetails(String userId) {
        log.info("Retrieving order details for user: {}", userId);
        Orders order = ordersRepository.findByUserId(userId);
        if (order != null) {
            log.info("Found order for user: {}", userId);
        } else {
            log.warn("No order found for user: {}", userId);
        }
        return order;
    }

    public void sendEmail(String to, String subject, String body){
        log.info("Sending email to: {}, subject: {}", to, subject);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}, error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public List<Orders> getOrderHistory(String userId) {
        log.info("Retrieving order history for user: {}", userId);
        
        Optional<OrderHistory> orderHistory = orderHistoryRepository.findById(userId);
        if (orderHistory.isPresent()) {
            List<Orders> orders = orderHistory.get().getOrders();
            Collections.reverse(orders);
            log.info("Retrieved {} orders from history for user: {}", orders.size(), userId);
            return orders;
        } else {
            log.info("No order history found for user: {}", userId);
            return new ArrayList<>();
        }
    }
}



