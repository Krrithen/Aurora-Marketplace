package com.example.Orders.entities;

import com.example.Orders.dto.OrderProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Document(collection = Orders.COLLECTION_NAME4)
@Schema(description = "Order entity")
public class Orders {
    public static final String COLLECTION_NAME4 = "Orders";

    @Id
    @Schema(description = "Unique order identifier")
    private String orderId;
    
    @NotBlank(message = "User ID is required")
    @Schema(description = "User identifier", example = "user123", required = true)
    private String userId;
    
    @NotNull(message = "Products list cannot be null")
    @NotEmpty(message = "Products list cannot be empty")
    @Schema(description = "List of products in the order", required = true)
    private List<OrderProductsDTO> productsDTOList;
    
    @Schema(description = "Date when the order was placed")
    private LocalDateTime dateOfOrder;
    
    @NotBlank(message = "Order status is required")
    @Pattern(regexp = "^(Payment Pending|Paid|Shipped|Delivered|Cancelled)$", 
             message = "Order status must be one of: Payment Pending, Paid, Shipped, Delivered, Cancelled")
    @Schema(description = "Current status of the order", example = "Payment Pending", required = true)
    private String orderStatus;
    
    @NotBlank(message = "Shipping address is required")
    @Size(min = 10, max = 200, message = "Shipping address must be between 10 and 200 characters")
    @Schema(description = "Shipping address for the order", example = "123 Main St, City, State 12345", required = true)
    private String shippingAddress;
    
    @Min(value = 1, message = "Product count must be at least 1")
    @Schema(description = "Total number of products in the order", example = "3")
    private Integer productCount;
    
    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    @Schema(description = "Total price of the order", example = "99.99")
    private Double totalPrice;
    
    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "^(Credit Card|Debit Card|PayPal|Cash on Delivery)$", 
             message = "Payment mode must be one of: Credit Card, Debit Card, PayPal, Cash on Delivery")
    @Schema(description = "Payment method used for the order", example = "Credit Card", required = true)
    private String modeOfPayment;
}