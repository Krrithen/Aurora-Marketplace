package com.example.auroramarketplace.entities;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@ToString
@Document(collection = Products.COLLECTION_NAME)
public class Products {

    public static final String COLLECTION_NAME = "Products";

    @Id
    private String productId;
    
    @TextIndexed(weight = 10)
    @Indexed
    private String productName;
    
    @TextIndexed(weight = 5)
    private String description;
    
    private String image;
    
    @Indexed
    private String category;
    
    @Indexed
    private String brand;
    
    @Indexed
    private String merchantId;
    
    @Indexed
    private Double price;
    
    @Indexed
    private Integer productQuantity;
    
    @Indexed
    private String status;
    
    private Double rating;
    
    private Integer reviewCount;
    
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
}