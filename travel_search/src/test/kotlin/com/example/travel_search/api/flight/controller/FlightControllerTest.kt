package com.example.travel_search.api.flight.controller

import com.example.travel_search.api.flight.domain.dto.FlightDto
import com.example.travel_search.api.flight.domain.dto.FlightSearchRequestDto
import com.example.travel_search.api.flight.service.FlightService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class FlightControllerTest{
    @InjectMocks
    private lateinit var flightController: FlightController

    @Mock
    private lateinit var flightService: FlightService

    @Test
    fun `Search Flight Test`(){
        // Given
        val flightSearchRequestDto = FlightSearchRequestDto(
            startCode = "ICN",
            endCode = "GMP",
            date = "20220101",
            adult = 1
        )

        // When
        `when`(flightService.searchFlight(any())).thenReturn(listOf(
            FlightDto(
                airlineName = "김포국제공항",
                price = "190,000",
                startRoute = "11:00",
                endRoute = "13:00"
            )
        ))

        // Then
        val result = flightController.searchFlight(flightSearchRequestDto)

        assertEquals(1, result.body?.size)
        assertEquals("김포국제공항", result.body?.get(0)?.airlineName)
        assertEquals("190,000", result.body?.get(0)?.price)
        assertEquals("11:00", result.body?.get(0)?.startRoute)
        assertEquals("13:00", result.body?.get(0)?.endRoute)
    }
}
