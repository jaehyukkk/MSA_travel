package com.example.travel_user_service.api.user.domain.dto

import com.example.travel_user_service.api.user.domain.entity.User

class RegisterRequestDto(
    val username: String,
    var password: String,
) {
    fun toEntity(): User {
        return User(
            username = username,
            password = password,
            roles = "ROLE_USER"
        )
    }
}
