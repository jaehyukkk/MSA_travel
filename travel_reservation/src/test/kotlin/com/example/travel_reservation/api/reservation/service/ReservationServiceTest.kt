package com.example.travel_reservation.api.reservation.service

import com.example.travel_reservation.api.passenger.domain.dto.PassengerRequestDto
import com.example.travel_reservation.api.passenger.domain.dto.PassengerResponseDto
import com.example.travel_reservation.api.passenger.service.PassengerService
import com.example.travel_reservation.api.payment.domain.dto.PaymentRequestDto
import com.example.travel_reservation.api.payment.service.PaymentService
import com.example.travel_reservation.api.reservation.domain.dto.ReservationDetailResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationRequestDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationSearchRequestDto
import com.example.travel_reservation.api.reservation.domain.repository.ReservationRepository
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.example.travel_reservation.global.domain.vo.PageVo
import com.example.travel_reservation.global.enums.CardCompany
import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import com.example.travel_reservation.global.utils.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ReservationServiceTest{

    @InjectMocks
    private lateinit var reservationService: ReservationService

    @Mock
    private lateinit var reservationRepository: ReservationRepository

    @Mock
    private lateinit var passengerService: PassengerService

    @Mock
    private lateinit var paymentService: PaymentService

    @Test
    fun `reservation create test`() {
        //given
        val reservationRequestDto = ReservationRequestDto(
            startRoute = "ICN",
            endRoute = "JFK",
            payment = PaymentRequestDto(
                cardNumber = "1234-1234-1234-1234",
                amount = 1000,
                cardCompany = CardCompany.NH_CARD,
            ),
            passengers = listOf(
                PassengerRequestDto(
                    englishLastname = "kim",
                    englishFirstname = "dohyun",
                    nationality = Nationality.SOUTH_KOREA,
                    dateOfBirth = "19990101",
                    gender = Gender.MAN,
                    isAdult = true
                ),
                PassengerRequestDto(
                    englishLastname = "kim",
                    englishFirstname = "dohyun",
                    nationality = Nationality.SOUTH_KOREA,
                    dateOfBirth = "19990101",
                    gender = Gender.MAN,
                    isAdult = true
                )

            )
        )
        val userId = 1L
        val resultReservationId = 3L
        //when
        val payment = reservationRequestDto.payment.toEntity().apply { id = 1L }
        `when`(paymentService.create(any())).thenReturn(payment)
        `when`(passengerService.create(any())).thenReturn(1L)
        `when`(reservationRepository.save(any())).thenReturn(reservationRequestDto.toEntity(
            payment = payment,
            userId = userId
        ).apply { id = resultReservationId })

        val result = reservationService.create(reservationRequestDto, userId)

        //then
        verify(passengerService, times(2)).create(any())
        verify(paymentService, times(1)).create(any())
        assertEquals(resultReservationId, result)
    }

    @Test
    fun `reservation cancel test`() {
        //given
        val reservationId = 1L
        val userId = 1L
        //when
        `when`(reservationRepository.getUserReservationPaymentId(any(), any())).thenReturn(1L)
        `when`(reservationRepository.cancel(any(), any())).thenReturn(1)
        doNothing().`when`(paymentService).cancel(any())
        reservationService.cancel(
            reservationId = reservationId,
            userId = userId
        )
        //then
        verify(reservationRepository, times(1)).getUserReservationPaymentId(any(), any())
        verify(reservationRepository, times(1)).cancel(any(), any())
        verify(paymentService, times(1)).cancel(any())
    }

    @Test
    fun `get user reservation page test`() {
        //given
        val userId = 1L
        val reservationList: MutableList<ReservationListResponseDto> = mutableListOf()
        for (i in 1..5) {
            reservationList.add(ReservationListResponseDto(
                id = i.toLong(),
                startRoute = "ICN",
                endRoute = "JFK",
                reservationNumber = "reservationNumber",
                reservationStatus = ReservationStatus.RESERVED,
                amount = 1000,
                passengerCount = "어른1,어린이1",
                createdDate = LocalDateTime.now()
            ))
        }

        val pageable = PageVo(1, 10)

        val pageResult = PageImpl(reservationList, pageable.pageable(), 10)
        //when
        `when`(reservationRepository.getUserReservationList(any(), any(), any())).thenReturn(pageResult)
        val result = reservationService.getUserReservationList(1L, pageable, ReservationSearchRequestDto())
        //then
        verify(reservationRepository, times(1)).getUserReservationList(any(), any(), any())
        assertNotNull(result)
        assertEquals(5, result.content.size)
        assertEquals(10, result.totalElements)
        assertEquals(1, result.totalPages)
        assertEquals(0, result.number)
        assertEquals(10, result.size)
        assertTrue(result.isFirst)
        assertTrue(result.isLast)
        assertFalse(result.hasNext())
        assertFalse(result.hasPrevious())
        assertEquals(1000, result.content[0].amount)
        assertEquals("reservationNumber", result.content[0].reservationNumber)
        assertEquals(ReservationStatus.RESERVED, result.content[0].reservationStatus)
        assertEquals("어른1,어린이1", result.content[0].passengerCount)
        assertNotNull(result.content[0].createdDate)
        assertEquals(1L, result.content[0].id)
        assertEquals(2L, result.content[1].id)
    }

    @Test
    fun `get user reservation detail test`() {
        //given
        val userId = 1L
        val reservationId = 1L
        //when
        `when`(reservationRepository.getUserReservationDetail(any(), any())).thenReturn(
            ReservationDetailResponseDto(
                id = 1L,
                startRoute = "ICN",
                endRoute = "JFK",
                reservationNumber = "reservationNumber",
                reservationStatus = ReservationStatus.RESERVED,
                amount = 1000,
                createdDate = LocalDateTime.now(),
                passengers = setOf(
                    PassengerResponseDto(
                        id = 1L,
                        englishLastname = "kim",
                        englishFirstname = "dohyun",
                        nationality = Nationality.SOUTH_KOREA,
                        gender = Gender.MAN,
                        dateOfBirth = "19990101",
                        isAdult = true
                    )
                )
            )
        )
        val result = reservationService.getUserReservationDetail(userId, reservationId)
        //then
        verify(reservationRepository, times(1)).getUserReservationDetail(any(), any())
        assertNotNull(result)
        assertEquals(1000, result.amount)
        assertEquals("reservationNumber", result.reservationNumber)
        assertEquals(ReservationStatus.RESERVED, result.reservationStatus)
        assertNotNull(result.createdDate)
        assertEquals(1L, result.id)
    }
}
