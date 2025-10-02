package com.example.Merchants.repository;

import com.example.Merchants.entities.Merchants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantsRepository extends MongoRepository<Merchants,String> {
}
