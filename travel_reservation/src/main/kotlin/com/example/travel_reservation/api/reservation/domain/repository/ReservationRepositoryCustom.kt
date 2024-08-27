package com.example.travel_reservation.api.reservation.domain.repository

import com.example.travel_reservation.api.reservation.domain.dto.ReservationDetailResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationSearchRequestDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReservationRepositoryCustom {
    fun getUserReservationList(userId: Long, pageable: Pageable, reservationSearchRequestDto: ReservationSearchRequestDto): Page<ReservationListResponseDto>

    fun getUserReservationPaymentId(userId: Long, reservationId: Long): Long?

    fun getUserReservationDetail(userId: Long, reservationId: Long): ReservationDetailResponseDto?
}
