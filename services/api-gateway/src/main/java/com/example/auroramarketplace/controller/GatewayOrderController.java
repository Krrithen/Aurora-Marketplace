package com.example.auroramarketplace.controller;

import com.example.auroramarketplace.dto.OrderRequest;
import com.example.auroramarketplace.service.grpc.OrderGrpcClient;
import com.example.orders.grpc.OrderItemProto;
import com.example.orders.grpc.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class GatewayOrderController {

    private final OrderGrpcClient orderGrpcClient;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        List<OrderItemProto> protoItems = request.items().stream()
                .map(item -> OrderItemProto.newBuilder()
                        .setProductId(item.productId())
                        .setProductName(item.productName())
                        .setQuantity(item.quantity())
                        .setPrice(item.price().toString())
                        .setMerchantId(item.merchantId())
                        .build())
                .collect(Collectors.toList());

        return orderGrpcClient.createOrder(request.userId(), protoItems);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId) {
        return orderGrpcClient.getOrder(orderId);
    }
}
