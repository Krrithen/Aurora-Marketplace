package com.example.auroramarketplace.repository;

import com.example.auroramarketplace.entities.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends MongoRepository<Products, String> {
    List<Products> findByCategory(String category);
    List<Products> findByProductName(String productName);
//    List<Products> findByPrice(Double price);
}