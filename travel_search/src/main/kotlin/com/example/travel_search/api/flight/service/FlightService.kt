package com.example.travel_search.api.flight.service

import com.example.travel_search.api.flight.domain.dto.FlightSearchRequestDto
import com.example.travel_search.api.flight.domain.dto.FlightDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class FlightService(
    private val restTemplate: RestTemplate
) {

    fun searchFlight(flightSearchRequestDto: FlightSearchRequestDto) : List<FlightDto>?{
        val url = "http://localhost:5000/api/flights"
        val uri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("startCode", flightSearchRequestDto.startCode)
            .queryParam("endCode", flightSearchRequestDto.endCode)
            .queryParam("date", flightSearchRequestDto.date)
            .queryParam("adult", flightSearchRequestDto.adult)
            .toUriString()

        val result = restTemplate.getForEntity(uri, Array<FlightDto>::class.java).body
        return result?.toList()
    }
}
