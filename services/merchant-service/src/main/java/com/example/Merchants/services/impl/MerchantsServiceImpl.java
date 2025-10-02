package com.example.Merchants.services.impl;

import com.example.Merchants.entities.Merchants;
import com.example.Merchants.repository.MerchantsRepository;
import com.example.Merchants.services.MerchantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MerchantsServiceImpl implements MerchantsService {

    @Autowired
    MerchantsRepository merchantsRepository;

    @Override
    public Merchants addMerchantDetails(Merchants merchants) {
        return merchantsRepository.save(merchants);
    }

    @Override
    public Optional<Merchants> getMerchantById(String merchantId) {
        return merchantsRepository.findById(merchantId);
    }


}