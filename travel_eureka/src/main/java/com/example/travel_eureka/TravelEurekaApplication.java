package com.example.travel_eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class TravelEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelEurekaApplication.class, args);
	}

}
