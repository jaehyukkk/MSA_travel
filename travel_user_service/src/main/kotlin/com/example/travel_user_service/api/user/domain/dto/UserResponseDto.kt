package com.example.travel_user_service.api.user.domain.dto

import com.example.travel_user_service.api.user.domain.entity.User

class UserResponseDto(
    val username: String,
    val roles: String
) {

    companion object{
        fun of(user: User): UserResponseDto {
            return UserResponseDto(
                username = user.username,
                roles = user.roles
            )
        }
    }
}
