package com.openweather.exam.data

import com.openweather.exam.BuildConfig

class WeatherRepository(
    private val userDao: UserDao,
    internal val weatherDao: WeatherDao,
    private val apiService: WeatherApiService
) {
    // --- AUTH ---
    suspend fun registerUser(username: String, password: String) {
        val hash = hashPassword(password)
        userDao.register(UserEntity(username, hash))
    }

    suspend fun loginUser(username: String, password: String): Boolean {
        val user = userDao.getUser(username) ?: return false
        return user.passwordHash == hashPassword(password)
    }

    private fun hashPassword(password: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    // --- WEATHER ---
    suspend fun getWeatherByCity(city: String): WeatherResponse {
        val apiKey = BuildConfig.OPENWEATHER_API_KEY
        return apiService.getWeatherByCity(city, "metric", apiKey)
    }

    suspend fun fetchWeatherByCity(city: String, apiKey: String): WeatherResponse {
        val response = apiService.getWeatherByCity(city, "metric", apiKey)
        saveResponseToHistory(response)
        return response
    }

    private suspend fun saveResponseToHistory(response: WeatherResponse) {
        val firstWeather = response.weather?.firstOrNull()
        val entity = WeatherEntity(
            city = response.city ?: "Unknown",
            country = response.sys?.country ?: "",
            temp = response.main?.temp ?: 0.0,
            description = firstWeather?.description ?: "N/A",
            sunrise = response.sys?.sunrise ?: 0L,
            sunset = response.sys?.sunset ?: 0L,
            timestamp = response.timestamp ?: System.currentTimeMillis() / 1000
        )
        weatherDao.insert(entity)
    }

    fun getWeatherHistory() = weatherDao.getAll()
}
