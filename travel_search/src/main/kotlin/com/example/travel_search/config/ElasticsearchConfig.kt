package com.example.travel_search.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories
class ElasticsearchConfig {
//    override fun elasticsearchClient(): RestHighLevelClient {
//        return RestClients.create(ClientConfiguration.localhost()).rest()
//    }
}
