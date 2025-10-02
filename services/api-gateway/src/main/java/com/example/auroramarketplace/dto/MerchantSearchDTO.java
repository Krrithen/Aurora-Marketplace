package com.example.auroramarketplace.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
public class MerchantSearchDTO {
    @Id
    private String productId;
    private String merchantId;
    private String merchantName;
    private Double price;

}
