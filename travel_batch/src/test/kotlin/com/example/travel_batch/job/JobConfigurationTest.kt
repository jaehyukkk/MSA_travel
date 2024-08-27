package com.example.travel_batch.job

import com.example.travel_batch.util.RedisUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@EnableBatchProcessing
@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension::class)
class JobConfigurationTest{

    //TODO: 잡이 하나 더 생겼을 때 스코프 분리

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @MockBean
    private lateinit var redisUtil: RedisUtil

    @Test
    fun batch_test() {
        val jobExecution = jobLauncherTestUtils.launchJob()
        assertThat(jobExecution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
        verify(redisUtil).setObjectValues(any(), any())
    }
}
