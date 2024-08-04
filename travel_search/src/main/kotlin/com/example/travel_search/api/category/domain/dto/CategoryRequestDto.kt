package com.example.travel_search.api.category.domain.dto

import com.example.travel_search.api.airport.domain.domain.AirportRequestDto
import com.example.travel_search.api.airport.domain.entity.Airport
import com.example.travel_search.api.category.domain.entity.Category

class CategoryRequestDto(
    val name: String? = null,
    val airports: List<AirportRequestDto>? = null
) {

    fun toEntity() = Category(
        name = name!!,
    )
}
