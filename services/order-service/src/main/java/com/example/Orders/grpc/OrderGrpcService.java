package com.example.orders.grpc;

import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.service.OrdersService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrdersService ordersService;

    @Autowired
    public OrderGrpcService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Override
    public void createOrder(CreateOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            List<OrderItem> items = request.getItemsList().stream()
                    .map(proto -> OrderItem.builder()
                            .productId(proto.getProductId())
                            .productName(proto.getProductName())
                            .quantity(proto.getQuantity())
                            .price(new BigDecimal(proto.getPrice()))
                            .merchantId(proto.getMerchantId())
                            .build())
                    .collect(Collectors.toList());

            Order createdOrder = ordersService.addOrderProducts(items, request.getUserId());

            responseObserver.onNext(mapToOrderResponse(createdOrder));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to create order: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void getOrder(GetOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            Order order = ordersService.getOrderById(request.getOrderId());

            if (order != null) {
                responseObserver.onNext(mapToOrderResponse(order));
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Order not found")
                        .asRuntimeException());
            }
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to fetch order: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemProto> protoItems = order.getOrderItems().stream()
                .map(item -> OrderItemProto.newBuilder()
                        .setProductId(item.getProductId())
                        .setProductName(item.getProductName())
                        .setQuantity(item.getQuantity())
                        .setPrice(item.getPrice().toString())
                        .setMerchantId(item.getMerchantId())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.newBuilder()
                .setOrderId(order.getOrderId())
                .setUserId(order.getUserId())
                .setStatus(order.getOrderStatus())
                .setTotalPrice(order.getTotalPrice() != null ? order.getTotalPrice().toString() : "0.00")
                .addAllItems(protoItems)
                .build();
    }
}