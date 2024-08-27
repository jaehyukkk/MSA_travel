package com.example.travel_reservation.api.reservation.domain.repository

import com.example.travel_reservation.api.reservation.domain.entity.Reservation
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long>, ReservationRepositoryCustom{

    @Transactional
    @Modifying
    @Query("UPDATE Reservation r SET r.reservationStatus = :status WHERE r.id = :id")
    fun cancel(id: Long, status: ReservationStatus = ReservationStatus.CANCELLED) : Int
}


