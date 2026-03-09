package com.example.auroramarketplace.controller;

import com.example.auroramarketplace.dto.ProductRequest;
import com.example.auroramarketplace.service.grpc.CatalogGrpcClient;
import com.example.catalog.grpc.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GatewayCatalogController {

    private final CatalogGrpcClient catalogGrpcClient;

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return catalogGrpcClient.createProduct(
                request.name(),
                request.description(),
                request.price(),
                request.quantity(),
                request.categoryId(),
                request.attributes()
        );
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable String productId) {
        return catalogGrpcClient.getProduct(productId);
    }
}
