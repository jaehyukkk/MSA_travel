package com.example.travel_search.api.flight.controller

import com.example.travel_search.api.flight.domain.dto.FlightDto
import com.example.travel_search.api.flight.domain.dto.FlightSearchRequestDto
import com.example.travel_search.api.flight.domain.dto.FlightStatisticsDto
import com.example.travel_search.api.flight.domain.dto.FlightStatisticsSearchDto
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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

@WebMvcTest(FlightController::class)
class FlightControllerTest{

    @MockBean
    private lateinit var flightService: FlightService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Search Flight Test`(){
        // Given
        val flightSearchRequestDto = FlightSearchRequestDto(
            startCode = "ICN",
            endCode = "GMP",
            date = "20220101",
            adult = 1,
            child = 0
        )

        // When
        `when`(flightService.searchFlight(any())).thenReturn(listOf(
            FlightDto(
                airlineName = "김포국제공항",
                price = 190000,
                startRoute = "11:00",
                endRoute = "13:00"
            )
        ))

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/search/flight")
                .param("startCode", flightSearchRequestDto.startCode)
                .param("endCode", flightSearchRequestDto.endCode)
                .param("date", flightSearchRequestDto.date)
                .param("adult", flightSearchRequestDto.adult.toString())
                .param("child", flightSearchRequestDto.child.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].airlineName").value("김포국제공항"))
            .andExpect(jsonPath("$[0].price").value(190000))
            .andExpect(jsonPath("$[0].startRoute").value("11:00"))
            .andExpect(jsonPath("$[0].endRoute").value("13:00"))
            .andReturn()
    }

    @Test
    fun `Flight Statistics Test`(){
        // Given
        val flightStatisticsSearchDto = FlightStatisticsSearchDto(
            startCode = "ICN",
            endCode = "GMP",
        )

        val flightStatisticsDto = FlightStatisticsDto(
            startCode = "ICN",
            endCode = "GMP",
            statistics = listOf(
                FlightStatisticsDto.Companion.FlightStatistics("20240101", 190000.0),
                FlightStatisticsDto.Companion.FlightStatistics("20240102", 200000.0)
            )
        )

        // When
        `when`(flightService.getFlightStatistics(any())).thenReturn(flightStatisticsDto)

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/search/flight/statistics")
                .param("startCode", flightStatisticsSearchDto.startCode)
                .param("endCode", flightStatisticsSearchDto.endCode))
            .andExpect(status().isOk)
            .andExpect(jsonPath("startCode").value("ICN"))
            .andExpect(jsonPath("endCode").value("GMP"))
            .andExpect(jsonPath("statistics[0].date").value("20240101"))
            .andExpect(jsonPath("statistics[0].price").value(190000.0))
            .andExpect(jsonPath("statistics[1].date").value("20240102"))
            .andExpect(jsonPath("statistics[1].price").value(200000.0))
            .andReturn()
    }

}
