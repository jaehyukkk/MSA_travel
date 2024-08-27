package com.example.travel_reservation.api.reservation.controller

import com.example.travel_reservation.api.reservation.domain.dto.ReservationDetailResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationRequestDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationSearchRequestDto
import com.example.travel_reservation.api.reservation.service.ReservationService
import com.example.travel_reservation.context.CustomSecurityContextHolder
import com.example.travel_reservation.exception.BaseException
import com.example.travel_reservation.exception.ErrorCode
import com.example.travel_reservation.global.domain.vo.PageVo
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/reservation")
@RestController
class ReservationController(
    private val reservationService: ReservationService
) {

    @PostMapping()
    fun create(@RequestBody reservationRequestDto: ReservationRequestDto) : ResponseEntity<Long>{
        val userId = CustomSecurityContextHolder.getContext()?.id ?: throw BaseException(ErrorCode.UNAUTHORIZED)
        return ResponseEntity.ok(reservationService.create(reservationRequestDto, userId))
    }

    @PatchMapping("/{id}")
    fun cancel(@PathVariable id: Long) : ResponseEntity<Void>{
        val userId = CustomSecurityContextHolder.getContext()?.id ?: throw BaseException(ErrorCode.UNAUTHORIZED)
        reservationService.cancel(userId, id)
        return ResponseEntity.ok().build()
    }

    @GetMapping()
    fun getUserReservationList(pageVo: PageVo, reservationSearchRequestDto: ReservationSearchRequestDto) : ResponseEntity<Page<ReservationListResponseDto>> {
        val userId = CustomSecurityContextHolder.getContext()?.id ?: throw BaseException(ErrorCode.UNAUTHORIZED)
        return ResponseEntity.ok(reservationService.getUserReservationList(userId, pageVo, reservationSearchRequestDto))
    }

    @GetMapping("/{id}")
    fun getUserReservationDetail(@PathVariable id: Long) : ResponseEntity<ReservationDetailResponseDto> {
        val userId = CustomSecurityContextHolder.getContext()?.id ?: throw BaseException(ErrorCode.UNAUTHORIZED)
        return ResponseEntity.ok(reservationService.getUserReservationDetail(userId, id))
    }
}
