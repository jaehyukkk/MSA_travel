package com.example.travel_search.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig(
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplateBuilder = RestTemplateBuilder().build()
//        val restTemplate = restTemplateBuilder.build()
        val messageConverters = restTemplateBuilder.messageConverters

        messageConverters.add(0, MappingJackson2HttpMessageConverter(objectMapper))

        return restTemplateBuilder
    }
}
