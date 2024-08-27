package com.example.travel_reservation.api.passenger.domain.dto

import com.example.travel_reservation.api.passenger.domain.entity.Passenger
import com.example.travel_reservation.api.reservation.domain.entity.Reservation
import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Comment

class PassengerRequestDto(
    @Schema(description = "영문 성", example = "JANG")
    val englishFirstname: String,
    @Schema(description = "영문 이름", example = "JAEHYUK")
    val englishLastname: String,
    @Schema(description = "국적", example = "SOUTH_KOREA")
    val nationality: Nationality,
    @Schema(description = "성별", example = "MAN")
    val gender: Gender,
    @Schema(description = "생년월일", example = "19990101")
    val dateOfBirth: String,
    @Schema(description = "성인 여부", example = "true")
    val isAdult: Boolean
) {

    fun toEntity(reservation: Reservation) = Passenger(
        englishFirstname = englishFirstname,
        englishLastname = englishLastname,
        nationality = nationality,
        gender = gender,
        dateOfBirth = dateOfBirth,
        reservation = reservation,
        isAdult = isAdult
    )
}
