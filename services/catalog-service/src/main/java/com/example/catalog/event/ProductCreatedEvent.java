package com.example.catalog.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreatedEvent {
    private String id;
    private String name;
    private String description;
    private Double price;
    private String categoryId;
    private Map<String, String> attributes;
}
