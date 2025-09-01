package com.openweather.exam.data

import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

class WeatherRepository(
    private val userDao: UserDao,
    internal val weatherDao: WeatherDao
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
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    // --- WEATHER ---
    suspend fun fetchCurrentWeather(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        val response = RetrofitInstance.api.getCurrentWeather(lat, lon, "metric", apiKey)
        val firstWeather = response.weather?.firstOrNull()
        weatherDao.insert(
            WeatherEntity(
                city = response.city.toString(),
                country = response.sys?.country.toString(),
                temp = response.main?.temp,
                description = firstWeather?.description ?: "N/A",
                timestamp = response.timestamp
            )
        )
        return response
    }

    suspend fun getWeatherByCity(city: String, units: String, apiKey: String): WeatherResponse {
    val response = RetrofitInstance.api.getWeatherByCity(city, units, apiKey)
    // Save to database
    val firstWeather = response.weather?.firstOrNull()
    weatherDao.insert(
        WeatherEntity(
            city = response.city.toString(),
            country = response.sys?.country.toString(),
            temp = response.main?.temp,
            description = firstWeather?.description ?: "N/A",
            timestamp = response.timestamp
        )
    )
    return response
}

    fun getWeatherHistory(): Flow<List<WeatherEntity>> = weatherDao.getAll()
}
