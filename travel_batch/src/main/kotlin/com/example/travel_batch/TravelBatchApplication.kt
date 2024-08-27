package com.example.travel_batch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class TravelBatchApplication

fun main(args: Array<String>) {
	val exitCode = SpringApplication.exit(SpringApplication.run(TravelBatchApplication::class.java, *args))
	exitProcess(exitCode)
}
