package com.example.travel_reservation.api.payment.domain.entity

import com.example.travel_reservation.api.payment.domain.enums.PaymentStatus
import com.example.travel_reservation.global.domain.entity.GlobalEntity
import com.example.travel_reservation.global.enums.CardCompany
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val cardNumber: String,
    val amount: Int,
    val cardCompany: CardCompany,
    var paymentStatus: PaymentStatus = PaymentStatus.COMPLETED,
    ) : GlobalEntity(){
}
