package com.example.travel_reservation.api.reservation.domain.repository

import com.example.travel_reservation.api.passenger.domain.dto.PassengerResponseDto
import com.example.travel_reservation.api.passenger.domain.entity.QPassenger.passenger
import com.example.travel_reservation.api.reservation.domain.dto.QReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationDetailResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationListResponseDto
import com.example.travel_reservation.api.reservation.domain.dto.ReservationSearchRequestDto
import com.example.travel_reservation.api.reservation.domain.entity.QReservation.reservation
import com.example.travel_reservation.api.reservation.enums.ReservationSearchOption
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.example.travel_reservation.exception.BaseException
import com.example.travel_reservation.exception.ErrorCode
import com.example.travel_reservation.global.enums.Gender
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.set
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.hibernate.HibernateQueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ReservationRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ReservationRepositoryCustom{
//    CaseBuilder().`when`(dispatch.status.eq(DispatchStatus.COMPLETED)).then(dispatch.originalPrice).otherwise(0).sum(),

//    constant
    override fun getUserReservationList(userId: Long, pageable: Pageable, reservationSearchRequestDto: ReservationSearchRequestDto): Page<ReservationListResponseDto> {
        val query = queryFactory.select(
            QReservationListResponseDto(
                reservation.id,
                reservation.startRoute,
                reservation.endRoute,
                reservation.reservationNumber,
                reservation.reservationStatus,
                reservation.payment.amount,
                JPAExpressions.select(
                    Expressions.stringTemplate("concat({0}, {1}, {2}, {3}, {4})",
                        Expressions.constant("어른"),
                        CaseBuilder()
                            .`when`(passenger.isAdult.isTrue)
                            .then(1)
                            .otherwise(0)
                            .sum(),
                        Expressions.constant(","),
                        Expressions.constant("어린이"),
                        CaseBuilder()
                            .`when`(passenger.isAdult.isFalse)
                            .then(1)
                            .otherwise(0)
                            .sum(),
                    )

                )
                    .from(passenger)
                    .where(passenger.reservation.eq(reservation)),
                reservation.createdDate
            )
        ).from(reservation)
            .leftJoin(reservation.payment)
            .where(reservation.userId.eq(userId), search(reservationSearchRequestDto))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(reservation.id.desc())
            .fetch()

        val count = queryFactory.select(reservation.id.count())
            .from(reservation)
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {
            count
        }
    }

    override fun getUserReservationPaymentId(userId: Long, reservationId: Long): Long? {
        return queryFactory.select(reservation.payment.id)
            .from(reservation)
            .where(reservation.id.eq(reservationId), reservation.userId.eq(userId))
            .fetchOne()
    }

    override fun getUserReservationDetail(userId: Long, reservationId: Long): ReservationDetailResponseDto? {
        return queryFactory.selectFrom(reservation)
            .where(reservation.id.eq(reservationId), reservation.userId.eq(userId))
            .leftJoin(reservation.payment)
            .leftJoin(reservation.passengers, passenger)
            .transform(groupBy(reservation.id).list(
                Projections.constructor(
                    ReservationDetailResponseDto::class.java,
                    reservation.id,
                    reservation.startRoute,
                    reservation.endRoute,
                    reservation.reservationNumber,
                    reservation.reservationStatus,
                    reservation.payment.amount,
                    reservation.createdDate,
                    set(
                        Projections.constructor(
                            PassengerResponseDto::class.java,
                            passenger.id,
                            passenger.englishFirstname,
                            passenger.englishLastname,
                            passenger.nationality,
                            passenger.gender,
                            passenger.dateOfBirth,
                            passenger.isAdult
                        )
                    )
                )
            )).firstOrNull()
    }

    private fun search(reservationSearchRequestDto: ReservationSearchRequestDto): BooleanExpression? {
        if(reservationSearchRequestDto.searchKeyword.isNullOrBlank()) {
            return null
        }

        return when (reservationSearchRequestDto.searchOption) {
            ReservationSearchOption.RESERVATION_NUMBER -> {
                reservation.reservationNumber.eq(reservationSearchRequestDto.searchKeyword)
            }

            ReservationSearchOption.START_ROUTE -> {
                reservation.startRoute.contains(reservationSearchRequestDto.searchKeyword)
            }

            ReservationSearchOption.END_ROUTE -> {
                reservation.endRoute.contains(reservationSearchRequestDto.searchKeyword)
            }

            else -> {
                null
            }
        }
    }
}
