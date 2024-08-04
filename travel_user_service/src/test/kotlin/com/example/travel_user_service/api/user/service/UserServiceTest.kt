package com.example.travel_user_service.api.user.service

import com.example.travel_user_service.api.user.domain.dto.LoginRequestDto
import com.example.travel_user_service.api.user.domain.dto.RegisterRequestDto
import com.example.travel_user_service.api.user.domain.dto.TokenResponseDto
import com.example.travel_user_service.api.user.domain.entity.User
import com.example.travel_user_service.api.user.domain.repository.UserRepository
import com.example.travel_user_service.provider.TokenProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class UserServiceTest{
    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var tokenProvider: TokenProvider

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Test
    fun `User Register Test`() {
        // Given
        val registerRequestDto = RegisterRequestDto("test123", "123456")
        // When
        `when`(userRepository.save(any())).thenReturn(User(
            username = registerRequestDto.username,
            password = registerRequestDto.password,
            roles = "ROLE_USER"
        ))
        `when`(passwordEncoder.encode(any())).thenReturn("123456")

        // Then
        val result = userService.registerUser(registerRequestDto)

        verify(userRepository, times(1)).save(any())

        assertEquals(result.username, registerRequestDto.username)
        assertEquals(result.roles, "ROLE_USER")
    }

    @Test
    fun `User Login Test`() {
        // Given
        val loginRequestDto = LoginRequestDto("test123", "123456")
        val tokenResponseDto = TokenResponseDto(
            grantType = "testGrantType",
            accessToken = "testAccessToken",
            refreshToken = "testRefreshToken",
            accessTokenExpiresIn = 1000,
            refreshTokenExpiresIn = 2000
        )

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequestDto.username, loginRequestDto.password)
        val authentication = mock(Authentication::class.java)

        // When
        `when`(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication)
        `when`(tokenProvider.generateTokenDto(authentication)).thenReturn(tokenResponseDto)

        // Then
        val result = userService.login(loginRequestDto)

        verify(authenticationManager).authenticate(authenticationToken)
        verify(tokenProvider).generateTokenDto(authentication)

        assertEquals(result.grantType, tokenResponseDto.grantType)
        assertEquals(result.accessToken, tokenResponseDto.accessToken)
        assertEquals(result.refreshToken, tokenResponseDto.refreshToken)
        assertEquals(result.accessTokenExpiresIn, tokenResponseDto.accessTokenExpiresIn)
        assertEquals(result.refreshTokenExpiresIn, tokenResponseDto.refreshTokenExpiresIn)
    }
}
