package com.example.travel_search.api.category.service

import com.example.travel_search.api.airport.domain.domain.AirportRequestDto
import com.example.travel_search.api.airport.service.AirportService
import com.example.travel_search.api.category.domain.dto.CategoryRequestDto
import com.example.travel_search.api.category.domain.repository.CategoryRepository
import com.google.gson.Gson
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml")
@ExtendWith(SpringExtension::class)
@SpringBootTest
class CategoryServiceTest{

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var airportService: AirportService

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var categoryService: CategoryService

    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `test categoryLoad returns void`() {
        val uri = "http://localhost:5000/api/categories"
        val categoryRequestDto = arrayOf(
            CategoryRequestDto(
                "국내",
                listOf(
                    AirportRequestDto("SEL", "서울", "김포국제공항"),
                    AirportRequestDto("CJU", "제주", "제주국제공항"),
                )

            ),
            CategoryRequestDto(
                "일본",
                listOf(
                    AirportRequestDto("FUK", "후쿠오카", "후쿠오카공항"),
                    AirportRequestDto("OKA", "오키나와", "오키나와공항"),
                )
            ),

        )
        val expectResult = Gson().toJson(categoryRequestDto)

        mockServer.expect(requestTo(uri))
            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON))

        categoryService.categoryLoad()

    }

}
