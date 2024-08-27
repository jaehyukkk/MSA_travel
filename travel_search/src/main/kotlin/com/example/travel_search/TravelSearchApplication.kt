package com.example.travel_search

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TravelSearchApplication

fun main(args: Array<String>) {
	runApplication<TravelSearchApplication>(*args)
}
