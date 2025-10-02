package com.example.auroramarketplace.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "feignMerchant", url = "${services.merchant.url}")
public interface FeignMerchantService {

    @GetMapping("/getMerchantsById/{merchantId}")
    public ResponseEntity<Object> getMerchantsById(@PathVariable("merchantId") String merchantId);
}
