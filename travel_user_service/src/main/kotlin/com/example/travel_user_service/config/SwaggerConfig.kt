package com.example.travel_user_service.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${openapi.service.url}") private val url: String,
    @Value("\${openapi.service.title}") private val title: String,
    @Value("\${openapi.service.version}") private val version: String
) {
    private val jwtSchemeName = "JWT AUTH"
    private val securityRequirement = SecurityRequirement().addList(jwtSchemeName)

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(components)
        .addSecurityItem(securityRequirement)
        .info(apiInfo())
        .servers(listOf(Server().url(url)))

    private fun apiInfo() = Info()
        .title(title)
        .version(version)


    var accessTokenSecurityScheme: SecurityScheme = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .`in`(SecurityScheme.In.HEADER)
        .name(jwtSchemeName)


    var components: Components = Components()
        .addSecuritySchemes(jwtSchemeName, accessTokenSecurityScheme)
}
