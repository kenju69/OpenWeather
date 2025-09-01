package com.openweather.exam.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.openweather.exam.R
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.RetrofitInstance
import com.openweather.exam.data.WeatherEntity
import com.openweather.exam.databinding.ActivityWeatherSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WeatherSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherSearchBinding
    private val repo by lazy {
        val db = AppDatabase.getDatabase(this)
        com.openweather.exam.data.WeatherRepository(db.userDao(), db.weatherDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                fetchWeather(city)
            } else {
                Toast.makeText(this, "Enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun fetchWeather(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCity(
                    city,
                    "metric",
                    "91a7b39c1cfd41554426d2703075a759" // replace with your key in production
                )

                // Save to Room database
                val firstWeather = response.weather?.firstOrNull()
                response.city?.let { cityName ->
                    response.sys?.country?.let { country ->
                        val entity = WeatherEntity(
                            city = cityName,
                            country = country,
                            temp = response.main?.temp ?: 0.0,
                            description = firstWeather?.description ?: "N/A",
                            timestamp = response.timestamp ?: System.currentTimeMillis() / 1000
                        )
                        repo.weatherDao.insert(entity)
                    }
                }

                runOnUiThread {
                    updateUI(response)
                }

            } catch (e: Exception) {
                runOnUiThread {
                    binding.tvWeatherResult.text = "Error: ${e.message}"
                }
            }
        }
    }

    private fun updateUI(weather: com.openweather.exam.data.WeatherResponse) {
        val city = weather.city ?: "Unknown"
        val country = weather.sys?.country ?: ""
        val temp = weather.main?.temp?.toInt() ?: 0
        val description = weather.weather?.firstOrNull()?.description ?: "N/A"

        binding.tvWeatherResult.text = "$city, $country\n$temp°C\n$description"

        // Sun or moon icon logic
        val currentTimeSec = System.currentTimeMillis() / 1000
        val sunsetTime = weather.sys?.sunset ?: currentTimeSec
        val iconRes = if (currentTimeSec < sunsetTime) {
            R.drawable.ic_sun // your sun icon in drawable
        } else {
            R.drawable.ic_moon // your moon icon in drawable
        }
        binding.tvWeatherResult.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0)
    }
}
