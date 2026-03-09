package com.example.orders.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders",
       indexes = @Index(name = "idx_orders_user_status", columnList = "user_id, order_status"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true, length = 36)
    private String orderId;

    @Column(name = "user_id", nullable = false)
    @NotBlank(message = "User ID is required")
    private String userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "date_of_order")
    private LocalDateTime dateOfOrder;

    @Column(name = "order_status", nullable = false, length = 50)
    @NotBlank(message = "Order status is required")
    @Pattern(regexp = "^(Payment Pending|Paid|Shipped|Delivered|Cancelled)$",
             message = "Order status must be one of: Payment Pending, Paid, Shipped, Delivered, Cancelled")
    private String orderStatus;

    @Column(name = "shipping_address", length = 200)
    @Size(max = 200, message = "Shipping address must be at most 200 characters")
    private String shippingAddress;

    @Column(name = "product_count")
    @Min(value = 0, message = "Product count cannot be negative")
    private Integer productCount;

    @Column(name = "total_price", precision = 12, scale = 2)
    @DecimalMin(value = "0", message = "Total price cannot be negative")
    private java.math.BigDecimal totalPrice;

    @Column(name = "mode_of_payment", length = 50)
    @Pattern(regexp = "^(Credit Card|Debit Card|PayPal|Cash on Delivery)?$",
             message = "Payment mode must be one of: Credit Card, Debit Card, PayPal, Cash on Delivery")
    private String modeOfPayment;

    public static String generateOrderId() {
        return UUID.randomUUID().toString();
    }
}
