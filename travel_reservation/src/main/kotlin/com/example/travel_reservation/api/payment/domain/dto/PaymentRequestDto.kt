package com.example.travel_reservation.api.payment.domain.dto

import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.global.enums.CardCompany

class PaymentRequestDto(
    val cardNumber: String,
    val amount: Int,
    val cardCompany: CardCompany,
) {

    fun toEntity() = Payment(
        cardNumber = cardNumber,
        amount = amount,
        cardCompany = cardCompany
    )
}
