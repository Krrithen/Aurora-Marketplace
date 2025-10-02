package com.example.auroramarketplace.controller;

import com.example.auroramarketplace.dto.*;
import com.example.auroramarketplace.entities.MerchantInfo;
import com.example.auroramarketplace.entities.Products;
import com.example.auroramarketplace.feignclients.FeignMerchantService;
import com.example.auroramarketplace.feignclients.FeignSearchService;
import com.example.auroramarketplace.repository.MerchantInfoRepository;
import com.example.auroramarketplace.services.ProductsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/products")
@Validated
@Tag(name = "Products", description = "Product management operations")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductBusinessService productBusinessService;

    @PostMapping("/addProducts")
    @Operation(summary = "Add a new product", description = "Creates a new product with merchant validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    public ResponseEntity<String> addProductDetails(@Valid @RequestBody ProductsDTO productsDTO) {
        try {
            log.info("Adding new product: {}", productsDTO.getProductName());
            Products savedProduct = productBusinessService.createProduct(productsDTO);
            log.info("Product saved with ID: {}", savedProduct.getProductId());
            return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error adding product: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error adding product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<String> addProductFallback(ProductsDTO productsDTO, Exception ex) {
        log.warn("Fallback method called for addProductDetails due to: {}", ex.getMessage());
        return new ResponseEntity<>("Service temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @DeleteMapping("/deleteProduct/{productId}")
    @Operation(summary = "Delete a product", description = "Removes a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<String> deleteProduct(
            @Parameter(description = "Product ID to delete", required = true)
            @PathVariable("productId") @NotBlank String productId) {
        try {
            log.info("Deleting product: {}", productId);
            productsService.deleteProduct(productId);
            log.info("Product deleted successfully: {}", productId);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting product {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>("Error deleting product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetAllProducts")
    @Operation(summary = "Get all products", description = "Retrieves a list of all available products")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<List<Products>> getAllProducts() {
        try {
            log.info("Retrieving all products");
            List<Products> productList = productsService.allProducts();
            log.info("Retrieved {} products", productList.size());
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetProductsByProductId/{productId}")
    public ResponseEntity<Object> getProductsByProductId(@PathVariable("productId") String productId) {
        Optional<Products> productIdList = productsService.getProductsByProductId(productId);
        if(productIdList.isPresent()){
            log.debug("Product ID list: {}", productIdList);
            return new ResponseEntity<>(productIdList, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(null,HttpStatus.OK);
    }

    @GetMapping("/GetProductsByCategory/{category}")
    public ResponseEntity<List<Products>> getProductsByCategory(@PathVariable("category") String category) {
        List<Products> categoryList = productsService.getProductsByCategory(category);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }


    @GetMapping("/GetProductsBySearch/{productName}")
    @Operation(summary = "Search products by name", description = "Searches for products using the search service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search term"),
        @ApiResponse(responseCode = "503", description = "Search service unavailable")
    })
    public ResponseEntity<List<Products>> getProductsBySearch(
            @Parameter(description = "Product name to search for", required = true)
            @PathVariable("productName") @NotBlank @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Product name contains invalid characters") String productName) {
        try {
            log.info("Searching for products with name: {}", productName);
            List<Products> products = productBusinessService.searchProducts(productName);
            log.info("Search completed. Found {} products for term: {}", products.size(), productName);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error searching products for term {}: {}", productName, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<List<Products>> searchFallback(String productName, Exception ex) {
        log.warn("Search fallback called for term: {} due to: {}", productName, ex.getMessage());
        // Fallback to local search
        try {
            List<Products> localResults = productsService.getProductsByCategory(productName);
            return new ResponseEntity<>(localResults, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Fallback search also failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/reduceQuantity/{productId}")
    @Operation(summary = "Reduce product quantity", description = "Reduces the quantity of a product by 1")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quantity reduced successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<String> reduceQuantity(
            @Parameter(description = "Product ID to reduce quantity", required = true)
            @PathVariable("productId") @NotBlank String productId) {
        try {
            log.info("Reducing quantity for product: {}", productId);
            productBusinessService.reduceProductQuantity(productId);
            log.info("Quantity reduced for product: {}", productId);
            return new ResponseEntity<>("Quantity reduced successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error reducing quantity for product {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>("Error reducing quantity", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetPriceByProductId/{productId}")
    @Operation(summary = "Get product price", description = "Retrieves the price of a specific product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Price retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Double> getPriceByProductId(
            @Parameter(description = "Product ID to get price", required = true)
            @PathVariable("productId") @NotBlank String productId) {
        try {
            log.info("Getting price for product: {}", productId);
            Double price = productsService.getPriceByProductId(productId);
            if (price == null || price == 0.0) {
                log.warn("Product not found or price is 0: {}", productId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Retrieved price {} for product: {}", price, productId);
            return new ResponseEntity<>(price, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting price for product {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/SearchMidByName/{productName}")
    @Operation(summary = "Search merchants by product name", description = "Finds merchants selling products with the given name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Merchant search results retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search term")
    })
    public ResponseEntity<List<MerchantSearchDTO>> searchMidByName(
            @Parameter(description = "Product name to search merchants", required = true)
            @PathVariable("productName") @NotBlank @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Product name contains invalid characters") String productName) {
        try {
            log.info("Searching merchants by product name: {}", productName);
            List<MerchantSearchDTO> merchants = productBusinessService.searchMerchantsByProduct(productName);
            log.info("Found {} merchants for product: {}", merchants.size(), productName);
            return new ResponseEntity<>(merchants, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error searching merchants by product name {}: {}", productName, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}