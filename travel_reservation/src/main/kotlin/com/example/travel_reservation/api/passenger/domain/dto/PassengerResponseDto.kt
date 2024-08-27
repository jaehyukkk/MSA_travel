package com.example.travel_reservation.api.passenger.domain.dto

import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import io.swagger.v3.oas.annotations.media.Schema

data class PassengerResponseDto(
    val id: Long,
    val englishFirstname: String,
    val englishLastname: String,
    val nationality: Nationality,
    val gender: Gender,
    val dateOfBirth: String,
    val isAdult: Boolean
) {
}
