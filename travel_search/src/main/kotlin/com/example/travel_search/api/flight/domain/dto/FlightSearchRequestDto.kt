package com.example.travel_search.api.flight.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

class FlightSearchRequestDto(
    @Schema(description = "출발지 코드", example = "SEL")
    val startCode: String? = null,
    @Schema(description = "도착지 코드", example = "KIX")
    val endCode: String? = null,
    @Schema(description = "출발 날짜", example = "20240805")
    val date: String? = null,
    @Schema(description = "성인 인원", example = "1")
    val adult: Int? = null,
) {
}
