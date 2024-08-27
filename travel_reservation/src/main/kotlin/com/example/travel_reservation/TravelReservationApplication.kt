package com.example.travel_reservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class TravelReservationApplication

fun main(args: Array<String>) {
	runApplication<TravelReservationApplication>(*args)
}
