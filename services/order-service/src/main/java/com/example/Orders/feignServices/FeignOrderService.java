package com.example.Orders.feignServices;

import com.example.Orders.dto.ProductsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(value = "feignOrder",url="${services.cart.url}")
public interface FeignOrderService {

    @GetMapping("/GetAllCartProducts/{userId}")
    public ResponseEntity<List<ProductsDTO>> getAllCartProducts(@PathVariable("userId") String userId);

    @DeleteMapping("/DeleteByCartId/{cartId}")
    public ResponseEntity<String> deleteByCartId(@PathVariable("cartId") String cartId);

}
