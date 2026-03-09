package com.example.catalog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String categoryId;

    // Polymorphic attributes to handle different product types (e.g., Electronics vs Clothing)
    private Map<String, String> attributes;
}
