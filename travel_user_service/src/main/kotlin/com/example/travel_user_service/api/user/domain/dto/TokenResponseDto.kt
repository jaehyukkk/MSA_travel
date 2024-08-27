package com.example.travel_user_service.api.user.domain.dto

class TokenResponseDto(
    val grantType: String,
    val accessToken: String,
    var refreshToken: String? = null,
    val accessTokenExpiresIn: Long,
    val refreshTokenExpiresIn: Long? = null
) {
}
