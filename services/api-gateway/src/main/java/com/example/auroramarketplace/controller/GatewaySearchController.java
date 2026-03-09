package com.example.auroramarketplace.controller;

import com.example.auroramarketplace.service.grpc.SearchGrpcClient;
import com.example.search.grpc.ProductResult;
import com.example.search.grpc.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class GatewaySearchController {

    private final SearchGrpcClient searchGrpcClient;

    @GetMapping
    public List<ProductResultDTO> search(@RequestParam String q, 
                                         @RequestParam(defaultValue = "0") int page, 
                                         @RequestParam(defaultValue = "10") int size) {
        
        SearchResponse response = searchGrpcClient.searchProducts(q, page, size);
        
        // Convert Protobuf response to simple DTO for JSON output
        return response.getProductsList().stream()
                .map(p -> new ProductResultDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getScore()
                ))
                .collect(Collectors.toList());
    }

    public record ProductResultDTO(
        String id,
        String name,
        String description,
        double price,
        double score
    ) {}
}
