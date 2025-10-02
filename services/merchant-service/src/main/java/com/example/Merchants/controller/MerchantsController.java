package com.example.Merchants.controller;

import com.example.Merchants.entities.Merchants;
import com.example.Merchants.services.MerchantsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/merchants")
@Validated
@Tag(name = "Merchants", description = "Merchant management operations")
public class MerchantsController {
    @Autowired
    MerchantsService merchantsService;

    @PostMapping("/addMerchants")
    @Operation(summary = "Add a new merchant", description = "Creates a new merchant in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Merchant added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Merchants> addMerchantsDetails(@Valid @RequestBody Merchants merchants) {
        try {
            log.info("Adding new merchant: {}", merchants.getMerchantName());
            Merchants savedMerchant = merchantsService.addMerchantDetails(merchants);
            log.info("Merchant added successfully with ID: {}", savedMerchant.getMerchantId());
            return new ResponseEntity<>(savedMerchant, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error adding merchant: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getMerchantsById/{merchantId}")
    @Operation(summary = "Get merchant by ID", description = "Retrieves a merchant by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Merchant found"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "400", description = "Invalid merchant ID")
    })
    public ResponseEntity<Object> getMerchantsById(
            @Parameter(description = "Merchant ID to retrieve", required = true)
            @PathVariable("merchantId") @NotBlank String merchantId) {
        try {
            log.info("Retrieving merchant with ID: {}", merchantId);
            Optional<Merchants> merchantsList = merchantsService.getMerchantById(merchantId);
            if (merchantsList.isPresent()) {
                log.info("Merchant found: {}", merchantsList.get().getMerchantName());
                return new ResponseEntity<>(merchantsList.get(), HttpStatus.OK);
            } else {
                log.warn("Merchant not found with ID: {}", merchantId);
                return new ResponseEntity<>("Merchant not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error retrieving merchant {}: {}", merchantId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
