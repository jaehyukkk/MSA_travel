package com.example.travel_search.api.category.domain.repository
import com.example.travel_search.api.airport.domain.entity.Airport
import com.example.travel_search.api.category.domain.entity.Category
import com.example.travel_search.config.QuerydslConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.persistence.EntityManager

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml")
@Import(QuerydslConfig::class)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    fun setUp() {
        val category1 = Category(name = "Category1")
        val airport1 = Airport(location = "Location1", code = "Code1", name = "Airport1", category = category1)
        val airport2 = Airport(location = "Location2", code = "Code2", name = "Airport2", category = category1)
        val airport3 = Airport(location = "Location3", code = "Code3", name = "Airport3", category = category1)
        entityManager.persist(category1)
        entityManager.persist(airport1)
        entityManager.persist(airport2)
        entityManager.persist(airport3)
        entityManager.flush()
    }

    @Test
    fun `getCategories should return list of CategoryListResponseDto`() {
        // When
        entityManager.clear()
        val result = categoryRepository.getCategories()

        // Then
        assertThat(result).isNotEmpty
        assertThat(result).hasSize(1)
        val category1 = result.find { it.id == 1L }
        assertThat(category1).isNotNull
        assertThat(category1?.name).isEqualTo("Category1")
        assertThat(category1?.airports).hasSize(3)
    }
}
