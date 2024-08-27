package com.example.travel_reservation.api.reservation.domain.dto

import com.example.travel_reservation.api.passenger.domain.dto.PassengerResponseDto
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import java.time.LocalDateTime

class ReservationDetailResponseDto(
    val id: Long,
    val startRoute: String,
    val endRoute: String,
    val reservationNumber: String,
    val reservationStatus: ReservationStatus,
    val amount: Int,
    val createdDate: LocalDateTime,
    val passengers: Set<PassengerResponseDto> = LinkedHashSet()
) {
}
