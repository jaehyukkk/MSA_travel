package com.example.travel_search.api.flight.controller

import com.example.travel_search.api.flight.domain.dto.FlightDto
import com.example.travel_search.api.flight.domain.dto.FlightSearchRequestDto
import com.example.travel_search.api.flight.service.FlightService
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class FlightControllerTest{
    @InjectMocks
    private lateinit var flightController: FlightController

    @Mock
    private lateinit var flightService: FlightService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(flightController).build()
    }


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
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/search/flight")
                .content(Gson().toJson(flightSearchRequestDto))
                .contentType("application/json"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].airlineName").value("김포국제공항"))
            .andExpect(jsonPath("$[0].price").value("190,000"))
            .andExpect(jsonPath("$[0].startRoute").value("11:00"))
            .andExpect(jsonPath("$[0].endRoute").value("13:00"))
            .andReturn()
    }
}
