package com.example.Merchants.services;

import com.example.Merchants.entities.Merchants;

import java.util.Optional;

public interface MerchantsService {
    public Merchants addMerchantDetails(Merchants merchants);
    public Optional<Merchants> getMerchantById(String merchantId);
}