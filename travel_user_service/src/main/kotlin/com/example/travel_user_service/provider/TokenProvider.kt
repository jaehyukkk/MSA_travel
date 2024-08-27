package com.example.travel_user_service.provider

import com.example.travel_user_service.api.user.domain.dto.TokenResponseDto
import com.example.travel_user_service.exception.BaseException
import com.example.travel_user_service.exception.ErrorCode
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import org.springframework.security.core.authority.SimpleGrantedAuthority

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

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            throw JwtException("잘못된 JWT 서명입니다.")

        } catch (e: MalformedJwtException) {
            throw JwtException("잘못된 JWT 서명입니다.")

        } catch (e: ExpiredJwtException) {
            throw JwtException("만료된 JWT 토큰입니다.")

        } catch (e: UnsupportedJwtException) {
            throw JwtException("지원되지 않는 JWT 토큰입니다.")

        } catch (e: IllegalArgumentException) {
            throw JwtException("지원되지 않는 JWT 토큰입니다.")
        }
    }

    fun getAuthentication(accessToken: String): Authentication {
        val claims = parseClaims(accessToken)

        if (claims[AUTHORITIES_KEY] == null) {
            throw BaseException(ErrorCode.FORBIDDEN)
        }

        val authorities = claims[AUTHORITIES_KEY]
            .toString()
            .split(",")
            .map(::SimpleGrantedAuthority)
            .toList()

        val principal: UserDetails = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}

