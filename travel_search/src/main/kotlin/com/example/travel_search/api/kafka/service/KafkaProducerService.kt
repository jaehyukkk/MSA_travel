package com.example.travel_search.api.kafka.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    fun sendMessage(topic: String, message: Any) {
        kafkaTemplate.send(topic, message)
    }
}
