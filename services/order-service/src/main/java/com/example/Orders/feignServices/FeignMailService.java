package com.example.Orders.feignServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "feignMail",url="${services.user.url}")
public interface FeignMailService
{
    @GetMapping("/GetUserMailByUserId/{userId}")
    public ResponseEntity<String> getUserMailByUserId(@PathVariable("userId") String userId );

    @GetMapping("/GetUserNameByUserId/{userId}")
    public ResponseEntity<String> getUserNameByUserId(@PathVariable("userId") String userId );
}
