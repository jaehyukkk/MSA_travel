package com.example.travel_batch.dto

data class FlightStatisticsDto(
    val startCode: String,
    val endCode: String,
    val statistics: List<FlightStatistics> = emptyList(),
) {

    companion object{
        class FlightStatistics(
            val date: String,
            val price: Double
        ){
            companion object{
                fun of(date: String, price: Double): FlightStatistics {
                    return FlightStatistics(date, price)
                }
            }

        }
    }
}
