package com.example.travel_reservation.api.payment.service

import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.payment.domain.enums.PaymentStatus
import com.example.travel_reservation.api.payment.domain.repository.PaymentRepository
import com.example.travel_reservation.global.enums.CardCompany
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class PaymentServiceTest{
    @InjectMocks
    private lateinit var paymentService: PaymentService

    @Mock
    private lateinit var paymentRepository: PaymentRepository

    @Test
    fun `payment create test`() {
        //given
        val payment = Payment(
            cardNumber = "1234-1234-1234-1234",
            amount = 1000,
            cardCompany = CardCompany.NH_CARD
        )
        //when
        `when`(paymentRepository.save(any())).thenReturn(payment.apply {
            id = 1L
            paymentStatus = PaymentStatus.COMPLETED
        })
        val result = paymentService.create(payment)
        //then
        assertEquals(1L, result.id)
        assertEquals("1234-1234-1234-1234", result.cardNumber)
        assertEquals(1000, result.amount)
        assertEquals(CardCompany.NH_CARD, result.cardCompany)
        assertEquals(PaymentStatus.COMPLETED, result.paymentStatus)
    }
}
