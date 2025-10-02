package com.example.auroramarketplace.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HealthCheckConfig implements HealthIndicator {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ServiceProperties serviceProperties;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        boolean isHealthy = true;

        // Check MongoDB connection
        try {
            mongoTemplate.getCollectionNames();
            details.put("mongodb", "UP");
        } catch (Exception e) {
            log.error("MongoDB health check failed", e);
            details.put("mongodb", "DOWN - " + e.getMessage());
            isHealthy = false;
        }

        // Check external services
        details.put("merchant-service", checkServiceHealth(serviceProperties.getMerchant().getUrl()));
        details.put("search-service", checkServiceHealth(serviceProperties.getSearch().getUrl()));
        details.put("user-service", checkServiceHealth(serviceProperties.getUser().getUrl()));
        details.put("cart-service", checkServiceHealth(serviceProperties.getCart().getUrl()));
        details.put("order-service", checkServiceHealth(serviceProperties.getOrder().getUrl()));

        Status status = isHealthy ? Status.UP : Status.DOWN;
        return Health.status(status).withDetails(details).build();
    }

    private String checkServiceHealth(String serviceUrl) {
        try {
            String healthUrl = serviceUrl.replace("/merchants", "/actuator/health")
                                       .replace("/search", "/actuator/health")
                                       .replace("/users", "/actuator/health")
                                       .replace("/cart", "/actuator/health")
                                       .replace("/orders", "/actuator/health");
            
            restTemplate.getForObject(healthUrl, String.class);
            return "UP";
        } catch (Exception e) {
            log.warn("Service health check failed for {}: {}", serviceUrl, e.getMessage());
            return "DOWN - " + e.getMessage();
        }
    }
}
