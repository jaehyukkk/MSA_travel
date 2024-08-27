package com.example.travel_search.api.airport.domain.repository

import com.example.travel_search.api.airport.domain.entity.QAirport.airport
import com.querydsl.jpa.impl.JPAQueryFactory

class AirportRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AirportRepositoryCustom{

    override fun getAirportCodes(): List<String> {
        return queryFactory.select(airport.code)
            .from(airport)
            .fetch()
    }
}
