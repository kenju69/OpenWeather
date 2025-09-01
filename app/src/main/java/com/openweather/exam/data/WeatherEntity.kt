package com.openweather.exam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val country: String,
    val temp: Double?,
    val description: String,
    val sunrise: Long?,
    val sunset: Long?,
    val timestamp: Long?
)
