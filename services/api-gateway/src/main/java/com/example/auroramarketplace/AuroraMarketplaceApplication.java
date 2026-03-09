package com.example.auroramarketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

@SpringBootApplication
@EnableDiscoveryClient
public class AuroraMarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuroraMarketplaceApplication.class, args);
	}

	@Bean
	public ProtobufModule protobufModule() {
		return new ProtobufModule();
	}

}
