package com.example.travel_search.api.category.service

import com.example.travel_search.api.airport.service.AirportService
import com.example.travel_search.api.category.domain.dto.CategoryRequestDto
import com.example.travel_search.api.category.domain.entity.Category
import com.example.travel_search.api.category.domain.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val airportService: AirportService,
    private val restTemplate: RestTemplate
) {

    fun categoryLoad() {
        val url = "http://localhost:5000/api/categories"
        val response = restTemplate.getForEntity(url, Array<CategoryRequestDto>::class.java)
        response.body?.forEach {
            val category = categoryRepository.save(it.toEntity())
            it.airports?.forEach { airport ->
                airportService.create(airport.toEntity(category))
            }
        }
    }
}
