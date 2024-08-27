package com.example.travel_reservation.api.reservation.domain.dto

import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ReservationListResponseDto @QueryProjection constructor(
    val id: Long,
    val startRoute: String,
    val endRoute: String,
    val reservationNumber: String,
    val reservationStatus: ReservationStatus,
    val amount: Int,
    val passengerCount: String,
    val createdDate: LocalDateTime,
) {
}
