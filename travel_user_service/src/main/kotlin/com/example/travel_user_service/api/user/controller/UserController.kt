package com.example.travel_user_service.api.user.controller

import com.example.travel_user_service.api.user.domain.dto.*
import com.example.travel_user_service.api.user.domain.entity.User
import com.example.travel_user_service.api.user.service.UserService
import com.example.travel_user_service.context.CustomSecurityContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/user")
@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody userRegisterRequestDto: RegisterRequestDto) : ResponseEntity<UserResponseDto> {
        return ResponseEntity.ok(userService.registerUser(userRegisterRequestDto))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(userService.login(loginRequestDto))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshTokenReqeustDto: RefreshTokenReqeustDto) : ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(userService.refresh(refreshTokenReqeustDto))
    }

//    @GetMapping("/test")
//    fun test(): ResponseEntity<String> {
//        val id = CustomSecurityContextHolder.getContext()?.id
//        println(id)
//        return ResponseEntity.ok("test")
//    }
}
