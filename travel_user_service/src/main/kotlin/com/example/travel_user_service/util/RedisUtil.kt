package com.example.travel_user_service.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun getValues(key: String): Any? {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        return values[key]
    }

    //클래스타입에 직렬화 할 DTO 클래스를 넣어주면, 해당 클래스타입으로 반환
    fun <T> getObjectValue(key: String, classType: Class<T>): T? {
        val values: String? = redisTemplate.opsForValue().get(key) as String?
        if (values.isNullOrBlank()) {
            return null
        }
        val mapper = ObjectMapper()
        return mapper.readValue(values, classType)
    }

    fun setObjectValues(key: String, value: Any) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        val mapper = ObjectMapper()
        values[key] = mapper.writeValueAsString(value)
    }

    fun setValues(key: String, value: Any) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        values[key] = value
    }

    fun setValues(key: String, value: Any, timeout: Long, timeUnit: TimeUnit?) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        values[key, value, timeout] = timeUnit!!
    }

    fun delete(key: String) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        values[key, "", 1] = TimeUnit.MICROSECONDS
    }
}
