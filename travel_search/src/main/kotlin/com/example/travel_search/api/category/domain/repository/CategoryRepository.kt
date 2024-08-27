package com.example.travel_search.api.category.domain.repository

import com.example.travel_search.api.category.domain.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long>, CategoryRepositoryCustom {
}
