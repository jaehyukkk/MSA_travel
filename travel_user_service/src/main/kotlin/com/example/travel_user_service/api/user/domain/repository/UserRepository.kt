package com.example.travel_user_service.api.user.domain.repository

import com.example.travel_user_service.api.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
}
