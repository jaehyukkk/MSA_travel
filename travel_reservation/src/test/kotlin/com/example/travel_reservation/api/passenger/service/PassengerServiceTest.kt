package com.example.travel_reservation.api.passenger.service

import com.example.travel_reservation.api.passenger.domain.entity.Passenger
import com.example.travel_reservation.api.passenger.domain.repository.PassengerRepository
import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.reservation.domain.entity.Reservation
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.example.travel_reservation.global.enums.CardCompany
import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class PassengerServiceTest{

    @InjectMocks
    private lateinit var passengerService: PassengerService

    @Mock
    private lateinit var passengerRepository: PassengerRepository

    @Test
    fun `passenger create test`() {
        //given
        val passenger = Passenger(
            englishFirstname = "JANG",
            englishLastname = "JAEHYUK",
            nationality = Nationality.SOUTH_KOREA,
            gender = Gender.MAN,
            dateOfBirth = "19990101",
            isAdult = true,
            reservation = Reservation(
                id = 1L,
                startRoute = "ICN",
                endRoute = "JFK",
                userId = 1L,
                reservationNumber = "T-1234567",
                reservationStatus = ReservationStatus.RESERVED,
                payment = Payment(
                    id = 1L,
                    cardNumber = "1234-1234-1234-1234",
                    amount = 1000,
                    cardCompany = CardCompany.NH_CARD
                )
            )
        )
        val resultId = 3L
        //when
        `when`(passengerRepository.save(any())).thenReturn(passenger.apply {
            id = resultId
        })
        val result = passengerService.create(passenger)
        //then
        verify(passengerRepository, times(1)).save(any())
        assertEquals(resultId, result)
    }
}
