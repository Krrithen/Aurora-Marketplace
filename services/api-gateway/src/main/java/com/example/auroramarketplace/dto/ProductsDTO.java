package com.example.auroramarketplace.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Product data transfer object")
public class ProductsDTO {

    @Id
    @Schema(description = "Unique product identifier")
    private String productId;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Schema(description = "Product name", example = "iPhone 13", required = true)
    private String productName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Product description", example = "Latest iPhone with advanced features")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    @Schema(description = "Product price", example = "999.99", required = true)
    private Double price;
    
    @NotNull(message = "Product quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 9999, message = "Quantity cannot exceed 9999")
    @Schema(description = "Available quantity", example = "100", required = true)
    private Integer productQuantity;
    
    @Pattern(regexp = "^(https?://.*\\.(jpg|jpeg|png|gif|webp))?$", message = "Image must be a valid URL")
    @Schema(description = "Product image URL", example = "https://example.com/image.jpg")
    private String image;
    
    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    @Schema(description = "Product category", example = "Electronics", required = true)
    private String category;
    
    @Size(max = 50, message = "Brand name cannot exceed 50 characters")
    @Schema(description = "Product brand", example = "Apple")
    private String brand;
    
    @NotBlank(message = "Merchant ID is required")
    @Schema(description = "Merchant identifier", example = "merchant123", required = true)
    private String merchantId;
}