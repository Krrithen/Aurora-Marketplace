package com.example.Merchants.entities;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = Merchants.COLLECTION_NAME2)
public class Merchants {

    public static final String COLLECTION_NAME2 = "Merchants";

    @Id
    private String merchantId;
    private String merchantName;
    private String merchantProductId;
    private String merchantEmail;
}

