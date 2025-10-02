package com.example.auroramarketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableMongoAuditing
@EnableTransactionManagement
public class AuroraMarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuroraMarketplaceApplication.class, args);
	}

}
