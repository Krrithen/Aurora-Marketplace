package com.example.search.grpc;

import com.example.search.grpc.SearchServiceGrpc;
import com.example.search.grpc.*;
import com.example.search.entity.ProductIndex;
import com.example.search.repository.ProductIndexRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class SearchGrpcService extends SearchServiceGrpc.SearchServiceImplBase {

    private final ProductIndexRepository productIndexRepository;

    @Override
    public void searchProducts(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        try {
            String query = request.getQuery();
            
            // Simple fuzzy search using Spring Data naming convention
            List<ProductIndex> results = productIndexRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

            List<ProductResult> productResults = results.stream()
                    .map(product -> ProductResult.newBuilder()
                            .setId(product.getId())
                            .setName(product.getName())
                            .setDescription(product.getDescription() != null ? product.getDescription() : "")
                            .setPrice(product.getPrice() != null ? product.getPrice() : 0.0)
                            .setScore(1.0) // Placeholder score
                            .build())
                    .collect(Collectors.toList());

            SearchResponse response = SearchResponse.newBuilder()
                    .addAllProducts(productResults)
                    .setTotalHits(productResults.size())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Search failed: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
