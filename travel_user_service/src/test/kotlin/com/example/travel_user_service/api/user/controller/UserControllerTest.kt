package com.example.travel_user_service.api.user.controller

import com.example.travel_user_service.api.user.domain.dto.*
import com.example.travel_user_service.api.user.service.UserService
import com.google.gson.Gson
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@ExtendWith(MockitoExtension::class)
class UserControllerTest{

    @InjectMocks
    lateinit var userController: UserController

    @Mock
    lateinit var userService: UserService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    @Test
    fun `Register User Test`(){
        val registerRequestDto = RegisterRequestDto("testuser", "12345")

        `when`(userService.registerUser(any())).thenReturn(UserResponseDto(
            username = registerRequestDto.username,
            roles = "ROLE_USER"
        ))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user/register")
                .content(Gson().toJson(registerRequestDto))
                .contentType("application/json"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value(registerRequestDto.username))
            .andExpect(jsonPath("$.roles").value("ROLE_USER"))
            .andReturn()
    }

    @Test
    fun `Login User Test`() {
        // Given
        val loginRequestDto = LoginRequestDto("testuser", "12345")

        // When
        `when`(userService.login(any())).thenReturn(TokenResponseDto(
            grantType = "testGrantType",
            accessToken = "testAccessToken",
            refreshToken = "testRefreshToken",
            accessTokenExpiresIn = 1000,
            refreshTokenExpiresIn = 2000
        ))

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user/login")
                .content(Gson().toJson(loginRequestDto))
                .contentType("application/json"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.grantType").value("testGrantType"))
            .andExpect(jsonPath("$.accessToken").value("testAccessToken"))
            .andExpect(jsonPath("$.refreshToken").value("testRefreshToken"))
            .andExpect(jsonPath("$.accessTokenExpiresIn").value(1000))
            .andExpect(jsonPath("$.refreshTokenExpiresIn").value(2000))
            .andReturn()
    }

    @Test
    fun `Refresh Token Test`() {
        // Given
        val refreshTokenRequestDto = RefreshTokenReqeustDto(
            accessToken = "testAccessToken",
            refreshToken = "testRefreshToken"
        )

        // When
        `when`(userService.refresh(any())).thenReturn(TokenResponseDto(
            grantType = "testGrantType",
            accessToken = "newAccessToken",
            refreshToken = "testRefreshToken",
            accessTokenExpiresIn = 1000,
            refreshTokenExpiresIn = 2000
        ))

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user/refresh")
                .content(Gson().toJson(refreshTokenRequestDto))
                .contentType("application/json"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.grantType").value("testGrantType"))
            .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
            .andExpect(jsonPath("$.refreshToken").value("testRefreshToken"))
            .andExpect(jsonPath("$.accessTokenExpiresIn").value(1000))
            .andExpect(jsonPath("$.refreshTokenExpiresIn").value(2000))
            .andReturn()
    }
}
