package com.example.travel_reservation.context

class CustomSecurityContext(
    val id: Long? = null,
    val username: String? = null,
    val roles: List<String>? = null
) {
}

