package com.example.travel_search.api.airport.service

import com.example.travel_search.api.airport.domain.entity.Airport
import com.example.travel_search.api.airport.domain.repository.AirportRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class AirportServiceTest{

    @InjectMocks
    private lateinit var airportService: AirportService

    @Mock
    private lateinit var airportRepository: AirportRepository

    @Test
    fun `test create returns Airport`() {
        val airport = Airport(
            location = "인천",
            name = "인천국제공항",
            code = "ICN"
        )

        `when`(airportRepository.save(any())).thenReturn(airport)

        val result = airportService.create(airport)

        assertNotNull(result)
        assertEquals(result, airport)
    }

}
