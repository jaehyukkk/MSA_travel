package com.example.travel_reservation.global.utils

object Utils {

    fun generateReservationNumber(): String {
        return "R-${System.currentTimeMillis()}${(1..9).random()}"
    }
}
