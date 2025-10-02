package com.example.auroramarketplace.services;

import com.example.auroramarketplace.dto.*;
import com.example.auroramarketplace.entities.MerchantInfo;
import com.example.auroramarketplace.entities.Products;
import com.example.auroramarketplace.feignclients.FeignMerchantService;
import com.example.auroramarketplace.feignclients.FeignSearchService;
import com.example.auroramarketplace.repository.MerchantInfoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ProductBusinessService {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private FeignMerchantService feignMerchantService;

    @Autowired
    private FeignSearchService feignSearchService;

    @Autowired
    private MerchantInfoRepository merchantInfoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductBusinessService() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @CircuitBreaker(name = "merchant-service", fallbackMethod = "validateMerchantFallback")
    public boolean validateMerchant(String merchantId) {
        try {
            var response = feignMerchantService.getMerchantsById(merchantId);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error validating merchant {}: {}", merchantId, e.getMessage());
            return false;
        }
    }

    public boolean validateMerchantFallback(String merchantId, Exception ex) {
        log.warn("Merchant validation fallback called for {}: {}", merchantId, ex.getMessage());
        return false;
    }

    @CircuitBreaker(name = "search-service", fallbackMethod = "searchProductsFallback")
    public List<Products> searchProducts(String productName) {
        try {
            var response = feignSearchService.getSearchByName(productName).getBody();
            List<SolrDTO> solrDTO = objectMapper.convertValue(response, new TypeReference<List<SolrDTO>>(){});
            List<Products> products = new ArrayList<>();
            
            for (SolrDTO dto : solrDTO) {
                Optional<Products> product = productsService.getProductsByProductId(dto.getId());
                if (product.isPresent()) {
                    Products productDetails = new Products();
                    BeanUtils.copyProperties(product.get(), productDetails);
                    products.add(productDetails);
                }
            }
            
            return products;
        } catch (Exception e) {
            log.error("Error searching products for term {}: {}", productName, e.getMessage());
            throw new RuntimeException("Search service unavailable", e);
        }
    }

    public List<Products> searchProductsFallback(String productName, Exception ex) {
        log.warn("Search fallback called for term: {} due to: {}", productName, ex.getMessage());
        return productsService.getProductsByCategory(productName);
    }

    public List<MerchantSearchDTO> searchMerchantsByProduct(String productName) {
        List<String> productIds = productsService.searchPidByName(productName);
        List<MerchantSearchDTO> merchantSearchDTOs = new ArrayList<>();
        
        for (String productId : productIds) {
            MerchantSearchDTO merchantSearchDTO = new MerchantSearchDTO();
            merchantSearchDTO.setProductId(productId);
            
            String merchantId = productsService.getMerchantIdByProductId(productId);
            merchantSearchDTO.setMerchantId(merchantId);
            merchantSearchDTO.setPrice(productsService.getPriceByProductId(productId));
            
            Optional<MerchantInfo> merchantInfo = merchantInfoRepository.findById(merchantId);
            String merchantName = merchantInfo.map(MerchantInfo::getMerchantName).orElse("Not Found");
            merchantSearchDTO.setMerchantName(merchantName);
            
            merchantSearchDTOs.add(merchantSearchDTO);
        }
        
        return merchantSearchDTOs;
    }

    @Transactional
    public Products createProduct(ProductsDTO productsDTO) {
        // Validate merchant exists
        if (!validateMerchant(productsDTO.getMerchantId())) {
            throw new IllegalArgumentException("Merchant not found: " + productsDTO.getMerchantId());
        }

        // Create product entity
        Products product = new Products();
        BeanUtils.copyProperties(productsDTO, product);
        Products savedProduct = productsService.addProductDetails(product);

        // Create merchant info
        MerchantInfo merchantInfo = new MerchantInfo();
        BeanUtils.copyProperties(productsDTO, merchantInfo);
        productsService.addMerchantDetails(merchantInfo);

        // Add to search index
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        productSearchDTO.setProductId(productsDTO.getProductId());
        productSearchDTO.setProductName(productsDTO.getProductName());
        
        try {
            feignSearchService.addProductDetails(productSearchDTO);
        } catch (Exception e) {
            log.warn("Failed to add product to search index: {}", e.getMessage());
        }

        return savedProduct;
    }

    @Transactional
    public void reduceProductQuantity(String productId) {
        Optional<Products> productOpt = productsService.getProductsByProductId(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        
        Products product = productOpt.get();
        if (product.getProductQuantity() <= 0) {
            throw new IllegalArgumentException("Cannot reduce quantity below zero for product: " + productId);
        }
        
        productsService.reduceQuantity(productId);
    }
}
