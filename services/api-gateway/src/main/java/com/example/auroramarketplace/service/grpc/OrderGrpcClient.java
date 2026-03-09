package com.example.auroramarketplace.service.grpc;

import com.example.orders.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderGrpcClient {

    @GrpcClient("order-service")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceStub;

    public OrderResponse createOrder(String userId, List<OrderItemProto> items) {
        CreateOrderRequest request = CreateOrderRequest.newBuilder()
                .setUserId(userId)
                .addAllItems(items)
                .build();
        return orderServiceStub.createOrder(request);
    }

    public OrderResponse getOrder(String orderId) {
        GetOrderRequest request = GetOrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        return orderServiceStub.getOrder(request);
    }
}
