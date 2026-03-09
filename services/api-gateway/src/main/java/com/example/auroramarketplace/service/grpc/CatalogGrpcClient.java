package com.example.auroramarketplace.service.grpc;

import com.example.catalog.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogGrpcClient {

    @GrpcClient("catalog-service")
    private CatalogServiceGrpc.CatalogServiceBlockingStub catalogServiceStub;

    public ProductResponse createProduct(String name, String description, double price, int quantity, String categoryId, java.util.Map<String, String> attributes) {
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setQuantity(quantity)
                .setCategoryId(categoryId)
                .putAllAttributes(attributes)
                .build();
        return catalogServiceStub.createProduct(request);
    }

    public ProductResponse getProduct(String productId) {
        GetProductRequest request = GetProductRequest.newBuilder()
                .setProductId(productId)
                .build();
        return catalogServiceStub.getProduct(request);
    }
}
