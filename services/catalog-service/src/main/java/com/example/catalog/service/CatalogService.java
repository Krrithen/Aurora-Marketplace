package com.example.catalog.service;

import com.example.catalog.entity.Product;
import java.util.List;

public interface CatalogService {
    Product createProduct(Product product);
    Product getProduct(String id);
    List<Product> listProducts(String categoryId);
    List<Product> batchGetProducts(List<String> ids);
}
