package com.example.travel_search.api.flight.controller

import com.example.travel_search.api.flight.domain.dto.FlightSearchRequestDto
import com.example.travel_search.api.flight.domain.dto.FlightDto
import com.example.travel_search.api.flight.domain.dto.FlightStatisticsDto
import com.example.travel_search.api.flight.domain.dto.FlightStatisticsSearchDto
import com.example.travel_search.api.flight.service.FlightService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/search/flight")
@RestController
class FlightController(
    private val flightService: FlightService
) {

    @GetMapping()
    fun searchFlight(flightSearchRequestDto: FlightSearchRequestDto) : ResponseEntity<List<FlightDto>> {
        return ResponseEntity.ok(flightService.searchFlight(flightSearchRequestDto))
    }

    @GetMapping("/statistics")
    fun getElasticsearchFlightStatisticsList(searchDto : FlightStatisticsSearchDto): ResponseEntity<FlightStatisticsDto?> {
        return ResponseEntity.ok(flightService.getFlightStatistics(searchDto))
    }
}
