package com.example.travel_reservation.api.reservation.domain.entity

import com.example.travel_reservation.api.passenger.domain.entity.Passenger
import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.example.travel_reservation.global.domain.entity.GlobalEntity
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Comment
import javax.persistence.*

@Entity
class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Comment("유저 ID")
    val userId: Long,
    @Comment("예약 번호")
    val reservationNumber: String,

    val startRoute: String,

    val endRoute: String,

    val reservationStatus: ReservationStatus = ReservationStatus.RESERVED,

    @JsonIgnore
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE], orphanRemoval = true)
    val passengers: MutableSet<Passenger>? = LinkedHashSet(),

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    val payment: Payment,

    ) : GlobalEntity() {
}
