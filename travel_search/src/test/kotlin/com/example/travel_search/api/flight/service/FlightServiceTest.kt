package com.example.travel_search.api.flight.service

import com.example.travel_search.api.flight.domain.dto.FlightDto
import com.example.travel_search.api.flight.domain.dto.FlightSearchRequestDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate

@ExtendWith(SpringExtension::class)
@SpringBootTest
class FlightServiceTest {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var flightService: FlightService

    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `test searchFlight returns flight data`() {
        // Given
        val flightSearchRequestDto = FlightSearchRequestDto("SEL", "KIX", "20240814", 1)
        val flightDto = FlightDto("김포국제공항", "190,000", "11:00", "13:00")

        val uri = "http://localhost:5000/api/flights?startCode=SEL&endCode=KIX&date=20240814&adult=1"
        val expectResult = "[{\"airline_name\":\"김포국제공항\",\"price\":\"190,000\",\"start_route\":\"11:00\",\"end_route\":\"13:00\"}]"

        mockServer.expect(requestTo(uri))
            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON))

        // When
        val result = flightService.searchFlight(flightSearchRequestDto)

        // Then
        assertEquals(1, result?.size)
        assertEquals(flightDto.airlineName, result?.get(0)?.airlineName)
        assertEquals(flightDto.price, result?.get(0)?.price)
        assertEquals(flightDto.startRoute, result?.get(0)?.startRoute)
        assertEquals(flightDto.endRoute, result?.get(0)?.endRoute)
    }
}
