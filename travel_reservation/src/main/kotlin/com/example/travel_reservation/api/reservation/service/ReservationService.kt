package com.example.travel_reservation.api.reservation.service

import com.example.travel_reservation.api.passenger.service.PassengerService
import com.example.travel_reservation.api.payment.service.PaymentService
import com.example.travel_reservation.api.reservation.domain.dto.ReservationDetailResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationRequestDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationSearchRequestDto
import com.example.travel_reservation.api.reservation.domain.repository.ReservationRepository
import com.example.travel_reservation.exception.BaseException
import com.example.travel_reservation.exception.ErrorCode
import com.example.travel_reservation.global.domain.vo.PageVo
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val passengerService: PassengerService,
    private val paymentService: PaymentService
) {

    @Transactional
    fun create(reservationRequestDto: ReservationRequestDto, userId: Long) : Long{
        val reservation = reservationRepository.save(reservationRequestDto.toEntity(
            payment = paymentService.create(reservationRequestDto.payment.toEntity()),
            userId = userId
        ))

        reservationRequestDto.passengers.forEach {
            passengerService.create(it.toEntity(reservation))
        }

        return reservation.id ?: throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    @Transactional
    fun cancel(userId: Long, reservationId: Long){
        paymentService.cancel(reservationRepository.getUserReservationPaymentId(userId, reservationId)
            ?: throw BaseException(ErrorCode.BAD_REQUEST))

        if (reservationRepository.cancel(reservationId) != 1) {
            throw BaseException(ErrorCode.BAD_REQUEST)
        }
    }

    fun getUserReservationList(userId: Long, pageVo: PageVo, reservationSearchRequestDto: ReservationSearchRequestDto) : Page<ReservationListResponseDto>{
        return reservationRepository.getUserReservationList(userId, pageVo.pageable(), reservationSearchRequestDto)
    }

    fun getUserReservationDetail(userId: Long, reservationId: Long) : ReservationDetailResponseDto {
        return reservationRepository.getUserReservationDetail(userId, reservationId)
            ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }
}
