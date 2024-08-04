package com.example.travel_user_service.api.user.service

import com.example.travel_user_service.api.user.domain.entity.User
import com.example.travel_user_service.api.user.domain.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService{

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return createUserDetails(userRepository.findByUsername(username)?:
        throw UsernameNotFoundException("$username -> Not Found in Database.")
        )
    }

    private fun createUserDetails(user: User): UserDetails {
        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority(user.roles)
        return org.springframework.security.core.userdetails.User(java.lang.String.valueOf(user.id),
            user.password, setOf(grantedAuthority))
    }
}
