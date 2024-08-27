package com.example.travel_search.api.flight.elasticsearchrepository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FlightElasticsearchRepositoryTest {

    @Autowired
    private lateinit var flightElasticsearchRepository: FlightElasticsearchRepository

    @Test
    fun testFindByDateAndStartCodeAndEndCode() {
        val date = "20240829"
        val startCode = "SEL"
        val endCode = "KIX"

        val result = flightElasticsearchRepository.findByDateAndStartCodeAndEndCode(date, startCode, endCode)

        assertEquals(1, result.size)
        assertEquals("피치항공", result[0].data[0].airlineName)
        assertEquals(84200, result[0].data[0].price)

    }
}
