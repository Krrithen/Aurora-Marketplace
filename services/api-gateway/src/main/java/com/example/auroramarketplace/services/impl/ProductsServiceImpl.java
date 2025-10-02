package com.example.auroramarketplace.services.impl;

import com.example.auroramarketplace.dto.MerchantDTO;
import com.example.auroramarketplace.dto.ProductDetailInfoDTO;
import com.example.auroramarketplace.dto.ProductsDTO;
import com.example.auroramarketplace.entities.MerchantInfo;
import com.example.auroramarketplace.entities.Products;
import com.example.auroramarketplace.feignclients.FeignMerchantService;
import com.example.auroramarketplace.feignclients.FeignSearchService;
import com.example.auroramarketplace.repository.MerchantInfoRepository;
import com.example.auroramarketplace.repository.ProductsRepository;
import com.example.auroramarketplace.services.ProductsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;


@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    MerchantInfoRepository merchantInfoRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FeignMerchantService feignMerchantService;

    @Autowired
    FeignSearchService feignSearchService;

    @Override
    public Products addProductDetails(Products products) {

        return productsRepository.save(products);
    }

    @Override
    public void deleteProduct(String productId) {
        productsRepository.deleteById(productId);
    }

    @Override
    public List<Products> allProducts() {
        return productsRepository.findAll();
    }

    @Override
    public List<Products> getProductsByCategory(String category) {
        return productsRepository.findByCategory(category);
    }

    @Override
    public Optional<Products> getProductsByProductId(String productId) {
        return productsRepository.findById(productId);
    }

    @Override
    public MerchantInfo addMerchantDetails(MerchantInfo merchantInfo) {
        return merchantInfoRepository.save(merchantInfo);
    }

    @Override
    public void reduceQuantity(String productId) {
        Optional<Products> productOpt = productsRepository.findById(productId);
        if (productOpt.isPresent()) {
            Products product = productOpt.get();
            int currentQuantity = product.getProductQuantity();
            if (currentQuantity > 0) {
                product.setProductQuantity(currentQuantity - 1);
                productsRepository.save(product);
            } else {
                throw new IllegalArgumentException("Cannot reduce quantity below zero for product: " + productId);
            }
        } else {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
    }



    //Merchant

    public List<String> searchPidByName(String productName) {
        List<Products> list = productsRepository.findByProductName(productName);
        List<String> pId = new ArrayList<>();
        for (Products product : list) {
            pId.add(product.getProductId());
        }
        return pId;
    }

    @Override
    public String getMerchantIdByProductId(String productId) {
        Optional<Products> productOpt = productsRepository.findById(productId);
        return productOpt.map(Products::getMerchantId).orElse("Not Found");
    }

    @Override
    public Double getPriceByProductId(String productId) {
        Optional<Products> productOpt = productsRepository.findById(productId);
        return productOpt.map(Products::getPrice).orElse(0.0);
    }

}