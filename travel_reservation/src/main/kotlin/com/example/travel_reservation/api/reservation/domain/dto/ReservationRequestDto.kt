package com.example.travel_reservation.api.reservation.domain.dto

import com.example.travel_reservation.api.passenger.domain.dto.PassengerRequestDto
import com.example.travel_reservation.api.payment.domain.dto.PaymentRequestDto
import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.reservation.domain.entity.Reservation
import com.example.travel_reservation.global.utils.Utils

class ReservationRequestDto(
    val startRoute: String,
    val endRoute: String,
    val payment: PaymentRequestDto,
    val passengers: List<PassengerRequestDto>
) {

    fun toEntity(payment: Payment, userId: Long) = Reservation(
        userId = userId,
        reservationNumber = Utils.generateReservationNumber(),
        payment = payment,
        startRoute = startRoute,
        endRoute = endRoute,
    )
}
