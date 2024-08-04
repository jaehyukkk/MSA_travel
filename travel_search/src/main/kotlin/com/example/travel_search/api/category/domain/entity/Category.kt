package com.example.travel_search.api.category.domain.entity

import com.example.travel_search.api.airport.domain.entity.Airport
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE], orphanRemoval = true)
    val airports: MutableSet<Airport>? = LinkedHashSet()
) {
}
