package com.example.travel_batch.job

import com.example.travel_batch.document.Flight
import com.example.travel_batch.dto.FlightStatisticsDto
import com.example.travel_batch.util.RedisUtil
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.elasticsearch.search.aggregations.metrics.ParsedAvg
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaCursorItemReader
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManagerFactory

@Configuration
@EnableBatchProcessing
class JobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val redisUtil: RedisUtil,
    private val elasticsearchOperations: ElasticsearchOperations,
    ) {

    @Bean
    fun simpleJob(): Job {
        return jobBuilderFactory.get("flight-statistics")
            .start(step1(null))
            .build()
    }

    @Bean
    @JobScope
    fun step1(
        @Value("#{jobParameters[datetime]}") datetime: String?,
    ): Step {
        return stepBuilderFactory.get("step1")
            .chunk<Array<Any>, FlightStatisticsDto>(3)
            .reader(categoryItemReader())
            .processor(combinationProcessor())
            .writer(combinationWriter())
            .transactionManager(transactionManager)
            .build()
    }

    @Bean
    fun categoryItemReader(): JpaCursorItemReader<Array<Any>> {
        return JpaCursorItemReaderBuilder<Array<Any>>()
            .name("categoryItemReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT c1.code, c2.code FROM Airport c1, Airport c2 WHERE c1.id < c2.id")
            .build()
    }

    @Bean
    fun combinationProcessor(): ItemProcessor<Array<Any>, FlightStatisticsDto> {
        return ItemProcessor { combination ->
            getElasticsearchFlightStatisticsList(
                startCode = combination[0].toString(),
                endCode = combination[1].toString()
            )
        }
    }

    @Bean
    fun combinationWriter(): ItemWriter<FlightStatisticsDto> {
        return ItemWriter { combinations ->
            combinations.filter{item -> item.statistics.isNotEmpty()}.forEach { combination ->
                redisUtil.setObjectValues("STATISTICS:${combination.startCode}-${combination.endCode}", combination)
            }
        }
    }

    fun getElasticsearchFlightStatisticsList(startCode: String, endCode:String): FlightStatisticsDto{

        val queryBuilder = NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("startCode", startCode))
                    .must(QueryBuilders.matchQuery("endCode", endCode))
            )
            .addAggregation(
                AggregationBuilders.terms("by_date").field("date")
                    .subAggregation(
                        AggregationBuilders.avg("average_price").field("data.price"))
            )

        val searchHits : SearchHits<Flight> = elasticsearchOperations.search(queryBuilder.build(), Flight::class.java)
        val terms = searchHits.aggregations?.get<ParsedStringTerms>("by_date")

        val statistics = terms?.buckets?.map{
            val avgPrice = it.aggregations.get<ParsedAvg>("average_price").value
            FlightStatisticsDto.Companion.FlightStatistics.of(it.keyAsString, avgPrice)
        } ?: emptyList()

        return FlightStatisticsDto(startCode, endCode, statistics)
    }

}
