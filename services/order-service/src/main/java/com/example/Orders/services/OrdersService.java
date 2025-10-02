package com.example.Orders.services;

import com.example.Orders.dto.OrderProductsDTO;
import com.example.Orders.dto.OrdersDTO;
import com.example.Orders.entities.Orders;

import java.util.List;

public interface OrdersService {

    public Orders addOrdersDetails(Orders orders);
    public List<Orders> getAllOrdersDetails();
    public Orders addOrderProducts(List<OrderProductsDTO> orderProductsDTOS,String userId);
    public Orders getOrdersDetails(String userId);
    public void sendEmail(String to, String subject, String body);
    public List<Orders> getOrderHistory(String userId);

}
