package com.example.travel_search.api.category.domain.repository

import com.example.travel_search.api.category.domain.dto.CategoryListResponseDto

interface CategoryRepositoryCustom {
    fun getCategories() : List<CategoryListResponseDto>
}
