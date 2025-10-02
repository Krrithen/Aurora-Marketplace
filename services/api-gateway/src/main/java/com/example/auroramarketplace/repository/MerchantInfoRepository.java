package com.example.auroramarketplace.repository;

import com.example.auroramarketplace.entities.MerchantInfo;
import com.example.auroramarketplace.entities.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantInfoRepository extends MongoRepository<MerchantInfo, String> {
}
