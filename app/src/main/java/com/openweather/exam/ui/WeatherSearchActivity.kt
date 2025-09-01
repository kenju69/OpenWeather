package com.openweather.exam.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.databinding.ActivityWeatherSearchBinding
import com.openweather.exam.viewmodel.WeatherViewModel
import com.openweather.exam.viewmodel.WeatherViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class WeatherSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherSearchBinding

    private val viewModel: WeatherViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        WeatherViewModelFactory(WeatherRepository(db.userDao(), db.weatherDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiKey = "YOUR_API_KEY" // Replace via README

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString()
            if (city.isNotEmpty()) {
                viewModel.fetchWeatherByCity(city, apiKey)
            } else {
                Toast.makeText(this, "Enter a city", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.weather.collectLatest { weather ->
                if (weather != null) {
                    binding.tvWeatherResult.text = buildString {
                        append("${weather.city}, ${weather.sys.country}\n")
                        append("Temp: ${weather.main.temp}°C\n")
                        append("Desc: ${weather.weather.firstOrNull()?.description ?: "N/A"}")
                    }
                }
            }
        }
    }
}
