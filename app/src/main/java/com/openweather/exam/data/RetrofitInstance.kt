package com.openweather.exam.data

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/"

    private val moshi = Moshi.Builder().build()

    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherApiService::class.java)
    }
}
