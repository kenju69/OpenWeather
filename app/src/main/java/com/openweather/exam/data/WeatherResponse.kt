package com.openweather.exam.data

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "name") val city: String? = null,
    @Json(name = "main") val main: Main? = null,
    @Json(name = "weather") val weather: List<Weather>? = null,
    @Json(name = "dt") val timestamp: Long? = null,
    @Json(name = "sys") val sys: Sys? = null
)

data class Main(
    @Json(name = "temp") val temp: Double? = null,
    @Json(name = "feels_like") val feelsLike: Double? = null,
    @Json(name = "temp_min") val tempMin: Double? = null,
    @Json(name = "temp_max") val tempMax: Double? = null,
    @Json(name = "pressure") val pressure: Int? = null,
    @Json(name = "humidity") val humidity: Int? = null
)

data class Weather(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "main") val main: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "icon") val icon: String? = null
)

data class Sys(
    @Json(name = "country") val country: String? = null,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null
)
