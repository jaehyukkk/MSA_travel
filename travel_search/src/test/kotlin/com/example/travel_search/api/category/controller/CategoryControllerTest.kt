package com.example.travel_search.api.category.controller

import com.example.travel_search.api.category.service.CategoryService
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class CategoryControllerTest{

    @InjectMocks
    private lateinit var categoryController: CategoryController

    @Mock
    private lateinit var categoryService: CategoryService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build()
    }


    @Test
    fun `Category Load Test`(){
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/search/category/load"))
            .andExpect(status().isOk)
            .andReturn()
    }
}
