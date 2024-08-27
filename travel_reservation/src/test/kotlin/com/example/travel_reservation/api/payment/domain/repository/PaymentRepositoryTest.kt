package com.example.travel_reservation.api.payment.domain.repository

import com.example.travel_reservation.api.payment.domain.entity.Payment
import com.example.travel_reservation.api.payment.domain.enums.PaymentStatus
import com.example.travel_reservation.config.JpaConfig
import com.example.travel_reservation.config.QuerydslConfig
import com.example.travel_reservation.global.enums.CardCompany
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import javax.persistence.EntityManager

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml")
@Import(
    QuerydslConfig::class,
    JpaConfig::class
)
@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private lateinit var paymentRepository: PaymentRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setUp() {
        val payment = Payment(
            cardNumber = "1234-1234-1234-1234",
            amount = 1000,
            cardCompany = CardCompany.NH_CARD,
            paymentStatus = PaymentStatus.COMPLETED
        )
        entityManager.persist(payment)
        entityManager.flush()
    }

    @Test
    fun `payment cancel test`() {
        paymentRepository.cancel(1L)
        entityManager.clear()
        val result = paymentRepository.findById(1L).get()
        assert(result.paymentStatus == PaymentStatus.CANCELLED)
    }
}
