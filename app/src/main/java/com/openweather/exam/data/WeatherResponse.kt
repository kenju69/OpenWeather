package com.openweather.exam.data

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "name") val city: String,
    @Json(name = "main") val main: Main,
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sys") val sys: Sys
)

data class Main(
    @Json(name = "temp") val temp: Double
)

data class Weather(
    @Json(name = "description") val description: String
)

data class Sys(
    @Json(name = "country") val country: String
)
