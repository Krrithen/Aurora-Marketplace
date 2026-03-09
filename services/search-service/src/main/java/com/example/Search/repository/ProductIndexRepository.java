package com.example.search.repository;

import com.example.search.entity.ProductIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductIndexRepository extends ElasticsearchRepository<ProductIndex, String> {
    List<ProductIndex> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
