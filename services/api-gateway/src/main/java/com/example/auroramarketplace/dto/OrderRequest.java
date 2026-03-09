package com.example.auroramarketplace.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
    String userId,
    List<OrderItemRequest> items
) {
    public record OrderItemRequest(
        String productId,
        String productName,
        int quantity,
        BigDecimal price,
        String merchantId
    ) {}
}
