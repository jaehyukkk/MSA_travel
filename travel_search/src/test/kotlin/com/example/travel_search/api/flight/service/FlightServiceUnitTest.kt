package com.example.travel_search.api.flight.service

import com.example.travel_search.api.flight.domain.dto.FlightStatisticsDto
import com.example.travel_search.api.flight.domain.dto.FlightStatisticsDto.Companion.FlightStatistics
import com.example.travel_search.api.flight.domain.dto.FlightStatisticsSearchDto
import com.example.travel_search.api.flight.elasticsearchrepository.FlightElasticsearchRepository
import com.example.travel_search.api.kafka.service.KafkaProducerService
import com.example.travel_search.util.RedisUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.web.client.RestTemplate

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension::class)
class FlightServiceUnitTest {

    @InjectMocks
    private lateinit var flightService: FlightService

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var kafkaProducerService: KafkaProducerService

    @Mock
    private lateinit var elasticsearchOperations: ElasticsearchOperations

    @Mock
    private lateinit var flightElasticsearchRepository: FlightElasticsearchRepository

    @Mock
    private lateinit var redisUtil: RedisUtil

    @Test
    fun `test getFlightStatistics returns flightStatistics data`() {

        //given
        val searchDto = FlightStatisticsSearchDto("SEL", "KIX")
        val flightStatisticsDto = FlightStatisticsDto(
            startCode = "SEL",
            endCode = "KIX",
            statistics = listOf(
                FlightStatistics("20240829", 190000.0),
                FlightStatistics("20240830", 200000.0)
            )
        )

        //when
        `when`(redisUtil.getObjectValue(any(), any<Class<FlightStatisticsDto>>())).thenReturn(flightStatisticsDto)
        val result = flightService.getFlightStatistics(searchDto)

        //then
        assertEquals(flightStatisticsDto.startCode, result?.startCode)
        assertEquals(flightStatisticsDto.endCode, result?.endCode)
        assertEquals(flightStatisticsDto.statistics.size, result?.statistics?.size)
        assertEquals(flightStatisticsDto.statistics[0].date, result?.statistics?.get(0)?.date)
        assertEquals(flightStatisticsDto.statistics[0].price, result?.statistics?.get(0)?.price)
    }
}
