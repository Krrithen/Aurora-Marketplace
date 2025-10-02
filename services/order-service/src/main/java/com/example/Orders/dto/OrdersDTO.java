package com.example.Orders.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
public class OrdersDTO {

    @Id
    private String orderId;
    private String userId;
    private List<OrderProductsDTO> orderProductsDTO;
    private Date dateOfOrder;
    private String orderStatus;
    private String shippingAddress;
    private Integer productCount;
    private Double totalPrice;
    private String modeOfPayment;
}
