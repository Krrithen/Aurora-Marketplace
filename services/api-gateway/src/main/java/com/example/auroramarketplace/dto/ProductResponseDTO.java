package com.example.auroramarketplace.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Product response data transfer object")
public class ProductResponseDTO {

    @Schema(description = "Unique product identifier", example = "prod123")
    private String productId;
    
    @Schema(description = "Product name", example = "iPhone 13")
    private String productName;
    
    @Schema(description = "Product description", example = "Latest iPhone with advanced features")
    private String description;
    
    @Schema(description = "Product price", example = "999.99")
    private Double price;
    
    @Schema(description = "Available quantity", example = "100")
    private Integer productQuantity;
    
    @Schema(description = "Product image URL", example = "https://example.com/image.jpg")
    private String image;
    
    @Schema(description = "Product category", example = "Electronics")
    private String category;
    
    @Schema(description = "Product brand", example = "Apple")
    private String brand;
    
    @Schema(description = "Merchant identifier", example = "merchant123")
    private String merchantId;
    
    @Schema(description = "Merchant name", example = "TechStore Inc")
    private String merchantName;
    
    @Schema(description = "Product creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Product last update timestamp")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Product status", example = "ACTIVE")
    private String status;
    
    @Schema(description = "Product rating", example = "4.5")
    private Double rating;
    
    @Schema(description = "Number of reviews", example = "150")
    private Integer reviewCount;
}
