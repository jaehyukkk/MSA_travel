package com.example.travel_reservation.api.reservation.domain.dto

import com.example.travel_reservation.api.reservation.enums.ReservationSearchOption

class ReservationSearchRequestDto(
    val searchOption : ReservationSearchOption? = null,
    val searchKeyword : String? = null
) {
}
