package com.example.travel_search.api.airport.domain.repository

import com.example.travel_search.api.airport.domain.entity.Airport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AirportRepository : JpaRepository<Airport, Long>, AirportRepositoryCustom {
}
