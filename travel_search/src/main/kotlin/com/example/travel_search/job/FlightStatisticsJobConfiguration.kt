//package com.example.travel_search.job
//
//import com.example.travel_search.api.flight.domain.dto.FlightStatisticsDto
//import com.example.travel_search.api.flight.service.FlightService
//import com.example.travel_search.util.RedisUtil
//import org.springframework.batch.core.Job
//import org.springframework.batch.core.Step
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
//import org.springframework.batch.item.ItemProcessor
//import org.springframework.batch.item.ItemWriter
//import org.springframework.batch.item.database.JpaCursorItemReader
//import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.transaction.PlatformTransactionManager
//import javax.persistence.EntityManagerFactory
//
//@Configuration
//@EnableBatchProcessing
//class FlightStatisticsJobConfiguration(
//    private val jobBuilderFactory: JobBuilderFactory,
//    private val stepBuilderFactory: StepBuilderFactory,
//    private val transactionManager: PlatformTransactionManager,
//    private val flightService: FlightService,
//    private val redisUtil: RedisUtil,
//    private val entityManagerFactory: EntityManagerFactory
//) {
//    @Bean
//    fun simpleJob(): Job {
//        return jobBuilderFactory.get("test1235645")
//            .start(step1())
//            .build()
//    }
//
//    @Bean
//    fun step1(): Step {
//        return stepBuilderFactory.get("step1")
//            .chunk<Array<Any>, FlightStatisticsDto>(3)
//            .reader(categoryItemReader())
//            .processor(combinationProcessor())
//            .writer(combinationWriter())
//            .transactionManager(transactionManager)
//            .build()
//    }
//
//    @Bean
//    fun categoryItemReader(): JpaCursorItemReader<Array<Any>> {
//        return JpaCursorItemReaderBuilder<Array<Any>>()
//            .name("categoryItemReader")
//            .entityManagerFactory(entityManagerFactory)
//            .queryString("SELECT c1.code, c2.code FROM Airport c1, Airport c2 WHERE c1.id < c2.id")
//            .build()
//    }
//
//    @Bean
//    fun combinationProcessor(): ItemProcessor<Array<Any>, FlightStatisticsDto> {
//        return ItemProcessor { combination ->
//            flightService.getElasticsearchFlightStatisticsList(
//                startCode = combination[0].toString(),
//                endCode = combination[1].toString()
//            )
//        }
//    }
//
//    @Bean
//    fun combinationWriter(): ItemWriter<FlightStatisticsDto> {
//        return ItemWriter { combinations ->
//            combinations.filter{item -> item.statistics.isNotEmpty()}.forEach { combination ->
//                redisUtil.setObjectValues("STATISTICS:${combination.startCode}-${combination.endCode}", combination)
//            }
//        }
//    }
//
//}
