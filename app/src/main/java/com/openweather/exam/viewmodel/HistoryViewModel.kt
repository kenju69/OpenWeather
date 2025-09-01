package com.openweather.exam.viewmodel

import androidx.lifecycle.ViewModel
import com.openweather.exam.data.WeatherEntity
import com.openweather.exam.data.WeatherRepository
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(private val repo: WeatherRepository) : ViewModel() {
    fun getHistory(): Flow<List<WeatherEntity>> = repo.getWeatherHistory()
}
