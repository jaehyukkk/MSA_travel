package com.example.travel_reservation.api.passenger.domain.repository

import com.example.travel_reservation.api.passenger.domain.entity.Passenger
import org.springframework.data.jpa.repository.JpaRepository

interface PassengerRepository : JpaRepository<Passenger, Long>{
}
