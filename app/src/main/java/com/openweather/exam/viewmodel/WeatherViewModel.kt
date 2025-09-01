package com.openweather.exam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.data.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    fun fetchWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            _weather.value = repo.fetchCurrentWeather(lat, lon, apiKey)
        }
    }
}
