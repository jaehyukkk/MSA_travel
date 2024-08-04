package com.example.travel_user_service.api.user.service

import com.example.travel_user_service.api.user.domain.dto.LoginRequestDto
import com.example.travel_user_service.api.user.domain.dto.RegisterRequestDto
import com.example.travel_user_service.api.user.domain.dto.TokenResponseDto
import com.example.travel_user_service.api.user.domain.dto.UserResponseDto
import com.example.travel_user_service.api.user.domain.entity.User
import com.example.travel_user_service.api.user.domain.repository.UserRepository
import com.example.travel_user_service.provider.TokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager : AuthenticationManager,
    private val tokenProvider: TokenProvider
    ) {

    fun registerUser(registerRequestDto: RegisterRequestDto) : UserResponseDto{
        registerRequestDto.password = passwordEncoder.encode(registerRequestDto.password)
        return UserResponseDto.of(userRepository.save(registerRequestDto.toEntity()))
    }

    fun login(loginRequestDto: LoginRequestDto): TokenResponseDto {
        val authenticationToken: UsernamePasswordAuthenticationToken = loginRequestDto.toAuthentication()
        val authentication: Authentication = authenticationManager.authenticate(authenticationToken)
        return tokenProvider.generateTokenDto(authentication)
    }
}
