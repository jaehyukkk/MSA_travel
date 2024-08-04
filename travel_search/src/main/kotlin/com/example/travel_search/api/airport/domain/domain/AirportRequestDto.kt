package com.example.travel_search.api.airport.domain.domain

import com.example.travel_search.api.airport.domain.entity.Airport
import com.example.travel_search.api.category.domain.entity.Category

class AirportRequestDto(
    val location: String? = null,
    val code: String? = null,
    val name: String? = null,
) {
    val category: Category? = null

    fun toEntity(category: Category) = Airport(
        location = location!!,
        code = code!!,
        name = name,
        category = category
    )
}
