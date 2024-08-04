package com.example.travel_user_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class TravelUserServiceApplication

fun main(args: Array<String>) {
	runApplication<TravelUserServiceApplication>(*args)
}
