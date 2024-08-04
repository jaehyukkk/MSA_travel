package com.example.travel_search.api.airport.domain.repository

import com.example.travel_search.api.airport.domain.entity.Airport
import org.springframework.data.jpa.repository.JpaRepository

interface AirportRepository : JpaRepository<Airport, Long> {
}
