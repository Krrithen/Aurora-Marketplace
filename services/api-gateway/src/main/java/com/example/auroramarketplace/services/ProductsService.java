package com.example.auroramarketplace.services;


import com.example.auroramarketplace.dto.MerchantDTO;
import com.example.auroramarketplace.entities.MerchantInfo;
import com.example.auroramarketplace.entities.Products;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;


public interface ProductsService {
    public Products addProductDetails(Products products);

    public void deleteProduct(String productId);

    public List allProducts();

    public List<Products> getProductsByCategory(String category);

    public Optional<Products> getProductsByProductId(String productId);

    public MerchantInfo addMerchantDetails(MerchantInfo merchantInfo);

    public void reduceQuantity(String productId);

    public List<String> searchPidByName(String productName);

    public String getMerchantIdByProductId(String productId);

    public Double getPriceByProductId(String productId);
}