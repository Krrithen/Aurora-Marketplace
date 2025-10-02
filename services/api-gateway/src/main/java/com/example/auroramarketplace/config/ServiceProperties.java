package com.example.auroramarketplace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "services")
@Validated
public class ServiceProperties {

    @NotNull
    private MerchantService merchant = new MerchantService();
    
    @NotNull
    private SearchService search = new SearchService();
    
    @NotNull
    private UserService user = new UserService();
    
    @NotNull
    private CartService cart = new CartService();
    
    @NotNull
    private OrderService order = new OrderService();

    @Data
    public static class MerchantService {
        @NotBlank(message = "Merchant service URL is required")
        private String url;
        private int timeout = 5000;
        private int retryAttempts = 3;
    }

    @Data
    public static class SearchService {
        @NotBlank(message = "Search service URL is required")
        private String url;
        private int timeout = 5000;
        private int retryAttempts = 3;
    }

    @Data
    public static class UserService {
        @NotBlank(message = "User service URL is required")
        private String url;
        private int timeout = 5000;
        private int retryAttempts = 3;
    }

    @Data
    public static class CartService {
        @NotBlank(message = "Cart service URL is required")
        private String url;
        private int timeout = 5000;
        private int retryAttempts = 3;
    }

    @Data
    public static class OrderService {
        @NotBlank(message = "Order service URL is required")
        private String url;
        private int timeout = 5000;
        private int retryAttempts = 3;
    }
}
