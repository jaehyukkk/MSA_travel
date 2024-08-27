package com.example.travel_search.api.flight.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

class FlightStatisticsSearchDto(
    @Schema(description = "출발지 코드", example = "SEL")
    val startCode: String,
    @Schema(description = "도착지 코드", example = "KIX")
    val endCode: String,
) {
}
