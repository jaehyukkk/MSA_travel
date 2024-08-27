package com.example.travel_search.api.airport.domain.domain

data class AirportListResponseDto(
    val id: Long,
    val location: String,
    val code: String,
    val name: String? = null,
) {
}
