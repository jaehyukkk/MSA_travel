package com.example.travel_search.api.airport.service

import com.example.travel_search.api.airport.domain.entity.Airport
import com.example.travel_search.api.airport.domain.repository.AirportRepository
import org.springframework.stereotype.Service

@Service
class AirportService(
    private val airportRepository: AirportRepository
) {

    fun create(airport : Airport) : Airport {
        return airportRepository.save(airport)
    }
}
