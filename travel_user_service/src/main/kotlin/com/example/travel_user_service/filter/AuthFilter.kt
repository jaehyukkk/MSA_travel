package com.example.travel_user_service.filter

import com.example.travel_user_service.context.CustomSecurityContext
import com.example.travel_user_service.context.CustomSecurityContextHolder
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class AuthFilter : Filter {

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val httpRequest = req as HttpServletRequest
        val id : Long? = httpRequest.getHeader("X-Id")?.toLong()
        val username: String? = httpRequest.getHeader("X-Username")
        val roles: String? = httpRequest.getHeader("X-Roles")

        val context = CustomSecurityContext(id, username, roles?.split(","))
        CustomSecurityContextHolder.setContext(context)

        println("AUTH")
        println(id)
        println(id)
        println(id)
        println(id)
        println(id)
        try {
            chain.doFilter(req, res)
        } finally {
            CustomSecurityContextHolder.clearContext()
        }
    }

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}
}
