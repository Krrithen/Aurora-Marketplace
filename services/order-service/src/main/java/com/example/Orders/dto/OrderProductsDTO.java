package com.example.Orders.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Order product data transfer object")
public class OrderProductsDTO {

    @Id
    @NotBlank(message = "Product ID is required")
    @Schema(description = "Unique product identifier", example = "prod123", required = true)
    private String productId;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Schema(description = "Product name", example = "iPhone 13", required = true)
    private String productName;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "999.99", required = true)
    private Double price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity cannot exceed 100")
    @Schema(description = "Product quantity", example = "1")
    private Integer quantity = 1;
    
    @NotBlank(message = "Merchant ID is required")
    @Schema(description = "Merchant identifier", example = "merchant123", required = true)
    private String merchantId;
}