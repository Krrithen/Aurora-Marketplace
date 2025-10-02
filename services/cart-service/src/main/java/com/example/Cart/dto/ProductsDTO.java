package com.example.Cart.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Product data transfer object for cart")
public class ProductsDTO {

    @Id
    @Schema(description = "Unique product identifier")
    private String productId;
    
    @Schema(description = "Product name", example = "iPhone 13")
    private String productName;
    
    @Schema(description = "Product description", example = "Latest iPhone with advanced features")
    private String description;
    
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "999.99")
    private Double price;
    
    @Min(value = 0, message = "Quantity cannot be negative")
    @Schema(description = "Product quantity", example = "1")
    private Integer productQuantity;
    
    @Schema(description = "Product image URL", example = "https://example.com/image.jpg")
    private String image;
    
    @Schema(description = "Product category", example = "Electronics")
    private String category;
    
    @Schema(description = "Product brand", example = "Apple")
    private String brand;
    
    @Schema(description = "Merchant identifier", example = "merchant123")
    private String merchantId;
}