package com.example.Orders.feignServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "feignProduct",url="${services.product.url}")

public interface FeignProductService {

    @GetMapping("/GetProductsByProductId/{productId}")
    public ResponseEntity<Object> getProductsByProductId(@PathVariable("productId") String productId);
}
