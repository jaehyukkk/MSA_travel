package com.example.travel_search.api.category.domain.repository

import com.example.travel_search.api.airport.domain.domain.AirportListResponseDto
import com.example.travel_search.api.airport.domain.entity.QAirport.airport
//import com.example.travel_search.api.airport.domain.entity.QAirport.airport
import com.example.travel_search.api.category.domain.dto.CategoryListResponseDto
import com.example.travel_search.api.category.domain.entity.QCategory.category
//import com.example.travel_search.api.category.domain.entity.QCategory.category
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.set
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CategoryRepositoryCustom {

    override fun getCategories() : List<CategoryListResponseDto> {
        return queryFactory.selectFrom(category)
            .leftJoin(category.airports, airport)
            .transform(groupBy(category.id).list(
                Projections.constructor(
                    CategoryListResponseDto::class.java,
                    category.id,
                    category.name,
                    set(
                        Projections.constructor(
                            AirportListResponseDto::class.java,
                            airport.id,
                            airport.location,
                            airport.code,
                            airport.name,
                        )
                    )
                )
            ))
    }
}
