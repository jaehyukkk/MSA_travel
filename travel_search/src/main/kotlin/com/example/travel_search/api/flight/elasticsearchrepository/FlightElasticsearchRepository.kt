package com.example.travel_search.api.flight.elasticsearchrepository

import com.example.travel_search.api.flight.domain.doc.FlightDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface FlightElasticsearchRepository : ElasticsearchRepository<FlightDocument, String>{
    fun findByDateAndStartCodeAndEndCode(date: String, startCode: String, endCode: String): List<FlightDocument>
}
