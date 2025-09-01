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

class WeatherSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherSearchBinding
    private val repo by lazy {
        val db = AppDatabase.getDatabase(this)
        com.openweather.exam.data.WeatherRepository(db.userDao(), db.weatherDao(), com.openweather.exam.data.RetrofitInstance.api)
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
                val response = RetrofitInstance.api.getWeatherByCity(city, "metric", "91a7b39c1cfd41554426d2703075a759")

                // Insert into Room
                val firstWeather = response.weather?.firstOrNull()
                val entity = WeatherEntity(
                    city = response.city.toString(),
                    country = response.sys?.country.toString(),
                    temp = response.main?.temp,
                    description = firstWeather?.description ?: "N/A",
                    sunrise = response.sys?.sunrise,
                    sunset = response.sys?.sunset,
                    timestamp = response.timestamp
                )
                val db = com.openweather.exam.data.AppDatabase.getDatabase(this@WeatherSearchActivity)
                val repo = com.openweather.exam.data.WeatherRepository(db.userDao(), db.weatherDao(), com.openweather.exam.data.RetrofitInstance.api)
                repo.weatherDao.insert(entity)

                runOnUiThread {
                    updateUI(entity)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.tvWeatherResult.text = "Error: ${e.message}"
                }
            }
        }
    }

    private fun updateUI(weather: WeatherEntity) {
        binding.tvWeatherResult.text = "${weather.city}, ${weather.country}\n${weather.temp?.toInt()}°C\n${weather.description}"

        // Determine icon
        val iconRes = when {
            weather.description.contains("rain", ignoreCase = true) -> R.drawable.ic_rain
            (System.currentTimeMillis() / 1000) < weather.sunset!! -> R.drawable.ic_sun
            else -> R.drawable.ic_moon
        }

        val drawable = resources.getDrawable(iconRes, null)
        val iconSize = (48 * resources.displayMetrics.density).toInt() // 48dp
        drawable.setBounds(0, 0, iconSize, iconSize)

        binding.tvWeatherResult.setCompoundDrawables(drawable, null, null, null)
        binding.tvWeatherResult.compoundDrawablePadding = (8 * resources.displayMetrics.density).toInt()
        binding.tvWeatherResult.gravity = android.view.Gravity.CENTER_VERTICAL
    }
}
