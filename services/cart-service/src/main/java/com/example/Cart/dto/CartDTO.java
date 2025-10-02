package com.example.Cart.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Cart data transfer object")
public class CartDTO {

    @Id
    @Schema(description = "Unique cart identifier")
    private String cartId;
    
    @NotBlank(message = "Product ID is required")
    @Schema(description = "Product identifier", example = "prod123", required = true)
    private String productId;
    
    @NotBlank(message = "User ID is required")
    @Schema(description = "User identifier", example = "user123", required = true)
    private String userId;
}
