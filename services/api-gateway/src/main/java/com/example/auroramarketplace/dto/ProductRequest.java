package com.example.auroramarketplace.dto;

import java.util.Map;

public record ProductRequest(
    String name,
    String description,
    double price,
    int quantity,
    String categoryId,
    Map<String, String> attributes
) {}
