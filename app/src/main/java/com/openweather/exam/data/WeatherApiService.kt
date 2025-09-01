package com.openweather.exam.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

