package com.example.auroramarketplace.entities;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = MerchantInfo.COLLECTION_NAME)
public class MerchantInfo {
    public static final String COLLECTION_NAME = "MerchantProductInfo";

    @Id
    private String merchantId;
    private String merchantName;
    private String ProductId;
    private String merchantEmail;
   private String merchantCode;
//    private double price;
//    private int productQuantity;
    //rating


}
