package com.example.travel_user_service.api.user.service

import com.example.travel_user_service.api.user.domain.dto.*
import com.example.travel_user_service.api.user.domain.entity.User
import com.example.travel_user_service.api.user.domain.repository.UserRepository
import com.example.travel_user_service.exception.BaseException
import com.example.travel_user_service.exception.ErrorCode
import com.example.travel_user_service.provider.TokenProvider
import com.example.travel_user_service.util.RedisUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager : AuthenticationManager,
    private val tokenProvider: TokenProvider,
    private val redisUtil: RedisUtil
    ) {

    fun registerUser(registerRequestDto: RegisterRequestDto) : UserResponseDto{
        registerRequestDto.password = passwordEncoder.encode(registerRequestDto.password)
        return UserResponseDto.of(userRepository.save(registerRequestDto.toEntity()))
    }

    fun login(loginRequestDto: LoginRequestDto): TokenResponseDto {
        val authenticationToken: UsernamePasswordAuthenticationToken = loginRequestDto.toAuthentication()
        val authentication: Authentication = authenticationManager.authenticate(authenticationToken)
        val tokenDto = tokenProvider.generateTokenDto(authentication)
        redisUtil.setValues(
            "RTK:${authentication.name}",
            tokenDto.refreshToken!!,
            tokenDto.refreshTokenExpiresIn!!,
            TimeUnit.MILLISECONDS
        )
        return tokenDto
    }

    fun refresh(refreshTokenReqeustDto: RefreshTokenReqeustDto) : TokenResponseDto {
        if (!tokenProvider.validateToken(refreshTokenReqeustDto.refreshToken)) {
            throw BaseException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        val authentication = tokenProvider.getAuthentication(refreshTokenReqeustDto.accessToken)
        val refreshToken = redisUtil.getValues("RTK:" + authentication.name) as String? ?: throw BaseException(ErrorCode.LOGOUT_USER)

        if (refreshTokenReqeustDto.refreshToken != refreshToken) {
            throw BaseException(ErrorCode.NOT_MATCH_USER)
        }

        val token = tokenProvider.generateTokenDto(authentication)
        token.refreshToken = refreshToken

        return token
    }
}
