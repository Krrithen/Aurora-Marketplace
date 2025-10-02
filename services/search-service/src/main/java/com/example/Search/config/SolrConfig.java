package com.example.Search.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class SolrConfig {
    
    @Value("${solr.url}")
    private String solrUrl;
    
    @Value("${solr.connection.timeout:10000}")
    private int connectionTimeout;
    
    @Value("${solr.socket.timeout:60000}")
    private int socketTimeout;
    
    @Value("${solr.max.connections:100}")
    private int maxConnections;
    
    @Value("${solr.max.connections.per.host:20}")
    private int maxConnectionsPerHost;
    
    @Bean
    public SolrClient solrClient() {  // to create Solr Client to access the Solr Database
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrUrl);
        
        // Configure connection timeouts
        builder.withConnectionTimeout(connectionTimeout);
        builder.withSocketTimeout(socketTimeout);
        
        // Configure connection pooling
        builder.withHttpClient(HttpSolrClient.builder()
            .withConnectionTimeout(connectionTimeout)
            .withSocketTimeout(socketTimeout)
            .withMaxConnections(maxConnections)
            .withMaxConnectionsPerHost(maxConnectionsPerHost)
            .build().getHttpClient());
        
        return builder.build();
    }
}
