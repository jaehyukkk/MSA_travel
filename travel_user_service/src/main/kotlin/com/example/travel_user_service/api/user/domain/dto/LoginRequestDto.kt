package com.example.travel_user_service.api.user.domain.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class LoginRequestDto(
    val username: String,
    val password: String
) {

    fun toAuthentication(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(username, password)
    }
}
