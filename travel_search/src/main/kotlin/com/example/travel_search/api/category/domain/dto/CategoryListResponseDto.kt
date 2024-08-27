package com.example.travel_search.api.category.domain.dto

import com.example.travel_search.api.airport.domain.domain.AirportListResponseDto
import com.example.travel_search.api.airport.domain.entity.Airport

class CategoryListResponseDto(
    val id: Long,
    val name: String,
    val airports: MutableSet<AirportListResponseDto>? = LinkedHashSet()
) {
}
