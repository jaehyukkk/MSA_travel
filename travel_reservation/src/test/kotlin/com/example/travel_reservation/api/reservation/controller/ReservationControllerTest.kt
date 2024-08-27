package com.example.travel_reservation.api.reservation.controller

import com.example.travel_reservation.api.passenger.domain.dto.PassengerRequestDto
import com.example.travel_reservation.api.passenger.domain.dto.PassengerResponseDto
import com.example.travel_reservation.api.payment.domain.dto.PaymentRequestDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationDetailResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationRequestDto
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.example.travel_reservation.api.reservation.service.ReservationService
import com.example.travel_reservation.global.domain.vo.PageVo
import com.example.travel_reservation.global.enums.CardCompany
import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(ReservationController::class)
class ReservationControllerTest{

    @MockBean
    private lateinit var reservationService: ReservationService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `reservation create test`() {
        //given
        val reservationRequestDto = ReservationRequestDto(
            startRoute = "ICN",
            endRoute = "JFK",
            payment = PaymentRequestDto(
                cardNumber = "1234-1234-1234-1234",
                amount = 1000,
                cardCompany = CardCompany.NH_CARD
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
        //when
        `when`(reservationService.create(any(), any())).thenReturn(1L)

        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/reservation")
                .header("X-Id", "1")
                .header("X-Username", "name")
                .header("X-Role", "ROLE_USER")
                .content(Gson().toJson(reservationRequestDto))
                .contentType("application/json"))
            .andExpect(jsonPath("$").value(1L))
            .andExpect(status().isOk)
    }

    @Test
    fun `reservation cancel test`() {
        val reservationId = 1L

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/reservation/$reservationId")
                .header("X-Id", "1")
                .header("X-Username", "name")
                .header("X-Role", "ROLE_USER")
                .contentType("application/json"))
            .andExpect(status().isOk)
    }

    @Test
    fun `get User Reservation Page`() {
        //given
        val pageVo = PageVo(1, 10)
        val userId = 1L
        val reservationList: MutableList<ReservationListResponseDto> = mutableListOf()
        for (i in 1..5) {
            reservationList.add(
                ReservationListResponseDto(
                    id = i.toLong(),
                    startRoute = "ICN",
                    endRoute = "JFK",
                    reservationNumber = "reservationNumber",
                    reservationStatus = ReservationStatus.RESERVED,
                    amount = 1000,
                    passengerCount = "어른1,어린이1",
                    createdDate = LocalDateTime.now()
            )
            )
        }

        val pageable = PageVo(1, 10)

        val pageResult = PageImpl(reservationList, pageable.pageable(), 10)
        `when`(reservationService.getUserReservationList(any(), any(), any())).thenReturn(pageResult)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/reservation")
                .header("X-Id", "1")
                .header("X-Username", "name")
                .header("X-Role", "ROLE_USER")
                .param("page", "1")
                .param("size", "10")
                .contentType("application/json"))
            .andExpect(jsonPath("$.content").isNotEmpty)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content[0].id").value(1L))
            .andExpect(jsonPath("$.content[0].reservationNumber").value("reservationNumber"))
            .andExpect(jsonPath("$.content[0].reservationStatus").value("RESERVED"))
            .andExpect(jsonPath("$.content[0].amount").value(1000))
            .andExpect(jsonPath("$.content[0].passengerCount").value("어른1,어린이1"))
            .andExpect(jsonPath("$.content[0].createdDate").isNotEmpty)
            .andExpect(status().isOk)
    }

    @Test
    fun `get User Reservation Detail`() {
        //given
        val detailResponse = ReservationDetailResponseDto(
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

        `when`(reservationService.getUserReservationDetail(1L, 1L)).thenReturn(detailResponse)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/reservation/1")
                .header("X-Id", "1")
                .header("X-Username", "name")
                .header("X-Role", "ROLE_USER")
                .contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.startRoute").value("ICN"))
            .andExpect(jsonPath("$.endRoute").value("JFK"))
            .andExpect(jsonPath("$.reservationNumber").value("reservationNumber"))
            .andExpect(jsonPath("$.reservationStatus").value("RESERVED"))
            .andExpect(jsonPath("$.amount").value(1000))
            .andExpect(jsonPath("$.createdDate").isNotEmpty)
            .andExpect(jsonPath("$.passengers").isArray)
            .andExpect(jsonPath("$.passengers[0].id").value(1L))
            .andExpect(jsonPath("$.passengers[0].englishLastname").value("kim"))
            .andExpect(jsonPath("$.passengers[0].englishFirstname").value("dohyun"))
            .andExpect(jsonPath("$.passengers[0].nationality").value("SOUTH_KOREA"))
            .andExpect(status().isOk)
    }

}
