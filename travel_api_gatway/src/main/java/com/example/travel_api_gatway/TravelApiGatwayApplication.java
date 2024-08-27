package com.example.travel_api_gatway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class TravelApiGatwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelApiGatwayApplication.class, args);
	}

}
