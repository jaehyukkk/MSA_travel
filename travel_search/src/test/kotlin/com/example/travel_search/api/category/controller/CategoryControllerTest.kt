package com.example.travel_search.api.category.controller

import com.example.travel_search.api.category.service.CategoryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class CategoryControllerTest{

    @InjectMocks
    private lateinit var categoryController: CategoryController

    @Mock
    private lateinit var categoryService: CategoryService


    @Test
    fun `Category Load Test`(){
        // Given

        // When

        // Then
    }
}
