package com.example.auroramarketplace.config;

import com.example.auroramarketplace.entities.Products;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "products";
    }

    @PostConstruct
    public void createIndexes() {
        try {
            MongoTemplate mongoTemplate = mongoTemplate();
            IndexOperations indexOps = mongoTemplate.indexOps(Products.class);

            // Text index for full-text search
            TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                    .onField("productName", 10)
                    .onField("description", 5)
                    .onField("category", 3)
                    .onField("brand", 2)
                    .build();
            indexOps.ensureIndex(textIndex);

            // Compound indexes for common queries
            CompoundIndexDefinition compoundIndex1 = new CompoundIndexDefinition(
                    org.springframework.data.mongodb.core.query.Criteria.where("category").is(1)
                            .and("productQuantity").is(1)
                            .and("status").is(1)
            );
            indexOps.ensureIndex(compoundIndex1);

            CompoundIndexDefinition compoundIndex2 = new CompoundIndexDefinition(
                    org.springframework.data.mongodb.core.query.Criteria.where("merchantId").is(1)
                            .and("status").is(1)
                            .and("productQuantity").is(1)
            );
            indexOps.ensureIndex(compoundIndex2);

            // Single field indexes
            indexOps.ensureIndex(new Index().on("price", org.springframework.data.domain.Sort.Direction.ASC));
            indexOps.ensureIndex(new Index().on("createdAt", org.springframework.data.domain.Sort.Direction.DESC));
            indexOps.ensureIndex(new Index().on("rating", org.springframework.data.domain.Sort.Direction.DESC));

            log.info("MongoDB indexes created successfully");
        } catch (Exception e) {
            log.error("Error creating MongoDB indexes: {}", e.getMessage());
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
