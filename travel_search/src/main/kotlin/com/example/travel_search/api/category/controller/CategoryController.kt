package com.example.travel_search.api.category.controller

import com.example.travel_search.api.category.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/search/category")
@RestController
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping("/load")
    fun categoryLoad() : ResponseEntity<Void>{
        categoryService.categoryLoad()
        return ResponseEntity.ok().build()
    }

    @GetMapping()
    fun getCategories() = ResponseEntity.ok(categoryService.getCategories())
}
