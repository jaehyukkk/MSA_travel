package com.example.travel_search.api.flight.domain.dto

data class FlightStatisticsDto(
    var startCode: String? = null,
    var endCode: String? = null,
    var statistics: List<FlightStatistics> = emptyList(),
) {

    companion object{
        class FlightStatistics(
            var date: String? = null,
            var price: Double? = null,
        ){
            companion object{
                fun of(date: String, price: Double): FlightStatistics {
                    return FlightStatistics(date, price)
                }
            }

        }
    }
}
