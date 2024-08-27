package com.example.travel_reservation.api.passenger.domain.entity

import com.example.travel_reservation.api.reservation.domain.entity.Reservation
import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Comment
import javax.persistence.*

@Entity
class Passenger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Comment("영문 성")
    val englishFirstname: String,
    @Comment("영문 이름")
    val englishLastname: String,
    @Comment("국적")
    val nationality: Nationality,
    @Comment("성별")
    val gender: Gender,
    @Comment("생년월일")
    val dateOfBirth: String,
    @Comment("성인 여부")
    val isAdult: Boolean,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    val reservation: Reservation,
) {
}
