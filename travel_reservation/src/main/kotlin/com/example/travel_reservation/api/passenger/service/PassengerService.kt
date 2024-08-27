package com.example.travel_reservation.api.passenger.service

import com.example.travel_reservation.api.passenger.domain.entity.Passenger
import com.example.travel_reservation.api.passenger.domain.repository.PassengerRepository
import org.springframework.stereotype.Service

@Service
class PassengerService(
    private val passengerRepository: PassengerRepository
) {

    fun create(passenger: Passenger): Long? {
        return passengerRepository.save(passenger).id
    }
}
