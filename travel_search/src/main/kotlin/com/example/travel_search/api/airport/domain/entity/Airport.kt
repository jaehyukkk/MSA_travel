package com.example.travel_search.api.airport.domain.entity

import com.example.travel_search.api.category.domain.entity.Category
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Airport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val location: String,
    val code: String,
    val name: String? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category? = null,
) {

}
