package com.example.auroramarketplace.dto;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class MerchantInfo {
    private String Id;
    private String merchantName;
    private String merchantId;
    private String merchantEmail;
//    private String merchantCode;
//    private double price;
//    private int stock;

}
