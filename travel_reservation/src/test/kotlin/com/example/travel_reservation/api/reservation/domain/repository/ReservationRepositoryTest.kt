package com.example.travel_reservation.api.reservation.domain.repository

import com.example.travel_reservation.api.passenger.domain.entity.Passenger
import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.payment.domain.enums.PaymentStatus
import com.example.travel_reservation.api.reservation.domain.dto.ReservationSearchRequestDto
import com.example.travel_reservation.api.reservation.domain.entity.Reservation
import com.example.travel_reservation.api.reservation.enums.ReservationStatus
import com.example.travel_reservation.config.JpaConfig
import com.example.travel_reservation.config.QuerydslConfig
import com.example.travel_reservation.global.domain.vo.PageVo
import com.example.travel_reservation.global.enums.CardCompany
import com.example.travel_reservation.global.enums.Gender
import com.example.travel_reservation.global.enums.Nationality
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import javax.persistence.EntityManager

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml")
@Import(
    QuerydslConfig::class,
    JpaConfig::class
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var reservationRepository: ReservationRepository

    @BeforeEach
    fun setUp() {
        val payment = Payment(
            cardNumber = "1234-1234-1234-1234",
            amount = 1000,
            cardCompany = CardCompany.NH_CARD,
            paymentStatus = PaymentStatus.COMPLETED
        )
        val reservation = Reservation(
            userId = 1L,
            startRoute = "ICN",
            endRoute = "JFK",
            reservationNumber = "reservationNumber",
            reservationStatus = ReservationStatus.RESERVED,
            payment = payment
        )

        val passenger = Passenger(
            englishLastname = "kim",
            englishFirstname = "dohyun",
            nationality = Nationality.SOUTH_KOREA,
            dateOfBirth = "19990101",
            gender = Gender.MAN,
            isAdult = true,
            reservation = reservation
        )
        val passenger1 = Passenger(
            englishLastname = "kim",
            englishFirstname = "yena",
            nationality = Nationality.SOUTH_KOREA,
            dateOfBirth = "20150202",
            gender = Gender.GIRL,
            isAdult = false,
            reservation = reservation
        )
        entityManager.persist(payment)
        entityManager.persist(reservation)
        entityManager.persist(passenger)
        entityManager.persist(passenger1)
        entityManager.flush()
    }


    @Test
    fun `getReservation should return page of ReservationResponseDto`() {
        val pageVo = PageVo(1, 10)
        val result = reservationRepository.getUserReservationList(1L, pageVo.pageable(), ReservationSearchRequestDto())

        assertThat(result.content).isNotEmpty
        assertThat(result.content).hasSize(1)
        val reservation = result.content.find { it.id == 1L }
        assertThat(reservation).isNotNull
        assertThat(reservation?.reservationNumber).isEqualTo("reservationNumber")
        assertThat(reservation?.reservationStatus).isEqualTo(ReservationStatus.RESERVED)
        assertThat(reservation?.amount).isEqualTo(1000)
        assertThat(reservation?.passengerCount).isEqualTo("어른1,어린이1")
    }

    @Test
    fun `get User Reservation`() {
        val result = reservationRepository.getUserReservationPaymentId(1L, 1L)
        assertThat(result).isEqualTo(1L)
    }

    @Test
    fun `reservation cancel test`() {
        val result = reservationRepository.cancel(1L)
        entityManager.clear()
        assertThat(result).isEqualTo(1)
        val reservation : Reservation = reservationRepository.findById(1L).orElseThrow()
        assertThat(reservation.reservationStatus).isEqualTo(ReservationStatus.CANCELLED)
    }

    @Test
    fun `get reservation detail test`() {
        val result = reservationRepository.getUserReservationDetail(1L, 1L)
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1L)
        assertThat(result?.startRoute).isEqualTo("ICN")
        assertThat(result?.endRoute).isEqualTo("JFK")
        assertThat(result?.reservationNumber).isEqualTo("reservationNumber")
        assertThat(result?.reservationStatus).isEqualTo(ReservationStatus.RESERVED)
        assertThat(result?.amount).isEqualTo(1000)
        assertThat(result?.createdDate).isNotNull
        assertThat(result?.passengers).isNotEmpty
        assertThat(result?.passengers?.size).isEqualTo(2)

    }

}
