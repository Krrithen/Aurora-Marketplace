package com.example.auroramarketplace.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class MerchantDTO {
    @Id
    private String merchantId;
    private String merchantName;
    private String ProductId;
    private String merchantEmail;
//    private Products productId;

}