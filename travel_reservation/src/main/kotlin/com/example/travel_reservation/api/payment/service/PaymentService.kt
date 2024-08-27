package com.example.travel_reservation.api.payment.service

import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.payment.domain.repository.PaymentRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    fun create(payment: Payment): Payment {
        return paymentRepository.save(payment)
    }

    fun cancel(id: Long) {
        paymentRepository.cancel(id)
    }
}
