package com.example.Search.controller;

import com.example.Search.dto.ProductDTO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;



@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SolrClient solrClient;

    @GetMapping("/query/{productName}")
    public ResponseEntity<?> searchProducts(@PathVariable("productName") String productName) {
        try {
            logger.info("Search request received for product: {}", productName);
            
            // Input validation and sanitization
            if (productName == null || productName.trim().isEmpty()) {
                logger.warn("Empty or null product name provided");
                return new ResponseEntity<>("Product name cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Sanitize input to prevent Solr injection attacks
            String sanitizedProductName = productName.trim()
                .replaceAll("[^a-zA-Z0-9\\s]", "") // Remove special characters except spaces
                .replaceAll("\\s+", " ") // Normalize whitespace
                .toLowerCase();
            
            if (sanitizedProductName.length() < 2) {
                logger.warn("Product name too short: {}", sanitizedProductName);
                return new ResponseEntity<>("Product name must be at least 2 characters", HttpStatus.BAD_REQUEST);
            }
            
            SolrQuery solrQuery = new SolrQuery();
            
            // Use proper Solr query syntax with fuzzy matching and wildcards
            String queryString = String.format("name:(*%s* OR %s~)", 
                sanitizedProductName, sanitizedProductName);
            solrQuery.setQuery(queryString);
            
            // Add query parameters for better performance
            solrQuery.setParam("defType", "edismax");  // Extended dismax query parser
            solrQuery.setParam("qf", "name^2");        // Query fields with boost
            solrQuery.setParam("pf", "name^3");        // Phrase fields with boost
            solrQuery.setParam("tie", "0.1");          // Tie breaker for scoring
            
            // Set result limits and sorting
            solrQuery.setRows(50);                     // Limit results
            solrQuery.setSort("score", SolrQuery.ORDER.desc); // Sort by relevance
            
            logger.debug("Executing Solr query: {}", queryString);
            QueryResponse response = solrClient.query(solrQuery);
            
            SolrDocumentList results = response.getResults();
            logger.info("Search completed. Found {} results for query: {}", results.size(), sanitizedProductName);
            
            return new ResponseEntity<SolrDocumentList>(results, HttpStatus.OK);
            
        } catch (SolrServerException e) {
            logger.error("Solr server error while searching for product: {}", productName, e);
            return new ResponseEntity<>("Search service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IOException e) {
            logger.error("IO error while searching for product: {}", productName, e);
            return new ResponseEntity<>("Search service connection error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error while searching for product: {}", productName, e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/addProducts")
    public ResponseEntity<String> addProductDetails(@RequestBody ProductDTO productDTO) {
        try {
            logger.info("Add product request received for ID: {}", 
                productDTO != null ? productDTO.getProductId() : "null");
            
            // Input validation
            if (productDTO == null || 
                productDTO.getProductId() == null || productDTO.getProductId().trim().isEmpty() ||
                productDTO.getProductName() == null || productDTO.getProductName().trim().isEmpty()) {
                logger.warn("Invalid product data provided: {}", productDTO);
                return new ResponseEntity<>("Invalid product data. Product ID and name are required.", HttpStatus.BAD_REQUEST);
            }
            
            // Sanitize input
            String sanitizedProductId = productDTO.getProductId().trim();
            String sanitizedProductName = productDTO.getProductName().trim()
                .replaceAll("[^a-zA-Z0-9\\s]", "") // Remove special characters except spaces
                .replaceAll("\\s+", " ") // Normalize whitespace
                .toLowerCase();
            
            if (sanitizedProductName.length() < 2) {
                logger.warn("Product name too short after sanitization: {}", sanitizedProductName);
                return new ResponseEntity<>("Product name must be at least 2 characters", HttpStatus.BAD_REQUEST);
            }
            
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", sanitizedProductId);
            doc.addField("name", sanitizedProductName);

            logger.debug("Adding product to Solr - ID: {}, Name: {}", sanitizedProductId, sanitizedProductName);
            solrClient.add(doc);
            solrClient.commit();
            
            logger.info("Product successfully added to Solr - ID: {}", sanitizedProductId);
            return new ResponseEntity<>("Data added to Solr successfully", HttpStatus.OK);
            
        } catch (SolrServerException e) {
            logger.error("Solr server error while adding product: {}", 
                productDTO != null ? productDTO.getProductId() : "unknown", e);
            return new ResponseEntity<>("Search service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IOException e) {
            logger.error("IO error while adding product: {}", 
                productDTO != null ? productDTO.getProductId() : "unknown", e);
            return new ResponseEntity<>("Search service connection error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error while adding product: {}", 
                productDTO != null ? productDTO.getProductId() : "unknown", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}