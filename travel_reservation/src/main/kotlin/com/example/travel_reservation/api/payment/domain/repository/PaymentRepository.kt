package com.example.travel_reservation.api.payment.domain.repository

import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.payment.domain.enums.PaymentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PaymentRepository : JpaRepository<Payment, Long>{

    @Modifying
    @Transactional
    @Query("UPDATE Payment p SET p.paymentStatus = :status WHERE p.id = :id")
    fun cancel(id: Long, status: PaymentStatus = PaymentStatus.CANCELLED) : Int
}
