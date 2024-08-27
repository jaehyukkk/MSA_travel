package com.example.travel_search.api.flight.domain.dto

class FlightMessageDto(
    val startCode: String,
    val endCode: String,
    val date: String,
    val data: List<FlightDto>
) {
}
