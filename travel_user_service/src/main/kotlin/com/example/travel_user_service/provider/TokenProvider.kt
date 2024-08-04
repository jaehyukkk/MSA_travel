package com.example.travel_user_service.provider

import com.example.travel_user_service.api.user.domain.dto.TokenResponseDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(
    @Value("\${jwt.secret}") secretKey: String,
) {

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "bearer"
        private const val ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 3
        private const val REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7
    }

    private val key: Key

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }


    fun generateTokenDto(authentication: Authentication): TokenResponseDto {

        // 권한들 가져오기
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        val now = Date().time

        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
        val refreshTokenExpiresIn = Date(now + REFRESH_TOKEN_EXPIRE_TIME)

        val accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return TokenResponseDto(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time,
            refreshTokenExpiresIn = refreshTokenExpiresIn.time
        )
    }
}

