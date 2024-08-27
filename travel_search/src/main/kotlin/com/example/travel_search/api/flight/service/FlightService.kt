package com.example.travel_search.api.flight.service

import com.example.travel_search.api.flight.domain.doc.FlightDocument
import com.example.travel_search.api.flight.domain.dto.*
import com.example.travel_search.api.flight.elasticsearchrepository.FlightElasticsearchRepository
import com.example.travel_search.api.kafka.service.KafkaProducerService
import com.example.travel_search.exception.BaseException
import com.example.travel_search.exception.ErrorCode
import com.example.travel_search.util.RedisUtil
import com.google.gson.Gson
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.elasticsearch.search.aggregations.metrics.ParsedAvg
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
//import org.springframework.data.elasticsearch.core.ElasticsearchAggregations
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class FlightService(
    private val restTemplate: RestTemplate,
    private val kafkaProducerService: KafkaProducerService,
    private val elasticsearchOperations: ElasticsearchOperations,
    private val flightElasticsearchRepository: FlightElasticsearchRepository,
    private val redisUtil: RedisUtil,
    @Value("\${api-server.url}") private val apiUrl: String

) {

    fun searchFlight(flightSearchRequestDto: FlightSearchRequestDto) : List<FlightDto>?{
        try{
            val url = "${apiUrl}/api/flights"
            val uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("startCode", flightSearchRequestDto.startCode)
                .queryParam("endCode", flightSearchRequestDto.endCode)
                .queryParam("date", flightSearchRequestDto.date)
                .queryParam("adult", flightSearchRequestDto.adult)
                .queryParam("child", flightSearchRequestDto.child)
                .toUriString()

            val result = restTemplate.getForEntity(uri, Array<FlightDto>::class.java).body?.toList()

            val flightMessageDto = FlightMessageDto(
                startCode = flightSearchRequestDto.startCode,
                endCode = flightSearchRequestDto.endCode,
                date = flightSearchRequestDto.date,
                data = result!!
            )
            val searchData = flightElasticsearchRepository.findByDateAndStartCodeAndEndCode(flightMessageDto.date, flightMessageDto.startCode, flightMessageDto.endCode)
            if(searchData.isEmpty()){
                kafkaProducerService.sendMessage("flight-data", flightMessageDto)
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun getFlightStatistics(flightStatisticsSearchDto: FlightStatisticsSearchDto): FlightStatisticsDto?{
        return redisUtil.getObjectValue("STATISTICS:${flightStatisticsSearchDto.startCode}-${flightStatisticsSearchDto.endCode}"
        , FlightStatisticsDto::class.java)
    }

    fun getElasticsearchFlightStatisticsList(startCode: String, endCode:String): FlightStatisticsDto{

        val queryBuilder = NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("startCode", startCode))
                    .must(QueryBuilders.matchQuery("endCode", endCode))
                )
            .addAggregation(
                AggregationBuilders.terms("by_date").field("date")
                    .subAggregation(
                        AggregationBuilders.avg("average_price").field("data.price"))
                )

        val searchHits : SearchHits<FlightDocument> = elasticsearchOperations.search(queryBuilder.build(), FlightDocument::class.java)
        val terms = searchHits.aggregations?.get<ParsedStringTerms>("by_date")

        val statistics = terms?.buckets?.map{
            val avgPrice = it.aggregations.get<ParsedAvg>("average_price").value
            FlightStatisticsDto.Companion.FlightStatistics.of(it.keyAsString, avgPrice)
        } ?: emptyList()

        return FlightStatisticsDto(startCode, endCode, statistics)
    }
}
