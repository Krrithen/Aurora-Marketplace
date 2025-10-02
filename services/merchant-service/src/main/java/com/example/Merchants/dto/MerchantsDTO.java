package com.example.Merchants.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Merchant data transfer object")
public class MerchantsDTO {
    @Id
    @Schema(description = "Unique merchant identifier")
    private String merchantId;
    
    @NotBlank(message = "Merchant name is required")
    @Size(min = 2, max = 100, message = "Merchant name must be between 2 and 100 characters")
    @Schema(description = "Merchant name", example = "TechStore Inc", required = true)
    private String merchantName;
    
    @Schema(description = "Merchant product identifier", example = "prod123")
    private String merchantProductId;
    
    @NotBlank(message = "Merchant email is required")
    @Email(message = "Merchant email must be valid")
    @Schema(description = "Merchant email address", example = "contact@techstore.com", required = true)
    private String merchantEmail;
}
