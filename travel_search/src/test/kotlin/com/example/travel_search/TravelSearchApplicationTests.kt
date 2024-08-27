package com.example.travel_search

import com.example.travel_search.api.flight.domain.dto.FlightStatisticsSearchDto
import com.example.travel_search.api.flight.service.FlightService
import org.junit.jupiter.api.Test
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TravelSearchApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Autowired
	lateinit var flightService: FlightService

	@Test
	fun test() {
		val searchDto = FlightStatisticsSearchDto("SEL", "KIX")

		val result = flightService.getFlightStatistics(searchDto)

		println(result)
	}


}
