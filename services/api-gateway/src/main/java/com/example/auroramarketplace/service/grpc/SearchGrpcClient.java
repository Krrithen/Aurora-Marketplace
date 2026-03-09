package com.example.auroramarketplace.service.grpc;

import com.example.search.grpc.SearchRequest;
import com.example.search.grpc.SearchResponse;
import com.example.search.grpc.SearchServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class SearchGrpcClient {

    @GrpcClient("search-service")
    private SearchServiceGrpc.SearchServiceBlockingStub searchServiceStub;

    public SearchResponse searchProducts(String query, int page, int size) {
        SearchRequest request = SearchRequest.newBuilder()
                .setQuery(query)
                .setPage(page)
                .setSize(size)
                .build();
        return searchServiceStub.searchProducts(request);
    }
}
