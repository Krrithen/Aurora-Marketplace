package com.example.Cart.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(value = "FeignCart", url = "${services.api-gateway.url}")
public interface FeignCartService {

    @GetMapping("/GetProductsByProductId/{productId}")
    public ResponseEntity<Object> getProductsByProductId(@PathVariable("productId") String productId);

    @PostMapping("/reduceQuantity/{productId}")
    public ResponseEntity<String> reduceQuantity(@PathVariable("productId") String productId);

    @GetMapping("/GetPriceByProductId/{productId}")
    public ResponseEntity<Double> getPriceByProductId(@PathVariable("productId") String productId);

}
