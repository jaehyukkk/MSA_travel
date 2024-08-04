package com.example.travel_user_service.api.user.domain.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(unique = true)
    private var username: String,
    private var password: String,
    val roles: String

) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val roleSet: MutableSet<GrantedAuthority> = HashSet()
        for (role in roles.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            roleSet.add(SimpleGrantedAuthority(role))
        }
        return roleSet
    }

    override fun getUsername(): String {
        return username
    }

    // 사용자의 password를 반환
    override fun getPassword(): String {
        return password
    }


    // 계정 만료 여부 반환
    override fun isAccountNonExpired(): Boolean {
        // 만료되었는지 확인하는 로직
        return true // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    override fun isAccountNonLocked(): Boolean {
        // 계정 잠금되었는지 확인하는 로직
        return true // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    override fun isCredentialsNonExpired(): Boolean {
        // 패스워드가 만료되었는지 확인하는 로직
        return true // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    override fun isEnabled(): Boolean {
        // 계정이 사용 가능한지 확인하는 로직
        return true // true -> 사용 가능
    }
}
