package com.example.Orders.entities;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Document(collection = OrderHistory.COLLECTION_NAME6)
public class OrderHistory {
    public static final String COLLECTION_NAME6 = "OrderHistory";

    @Id
    String orderId;
    String userId;
    List<Orders> orders;
}
