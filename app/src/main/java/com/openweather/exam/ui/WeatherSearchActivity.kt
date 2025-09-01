package com.openweather.exam.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openweather.exam.data.RetrofitInstance
import com.openweather.exam.databinding.ActivityWeatherSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString()
            fetchWeather(city)
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun fetchWeather(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCity(city, "metric", "91a7b39c1cfd41554426d2703075a759")
                runOnUiThread {
                    binding.tvWeatherResult.text = "Temp: ${response.main.temp}°C"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    binding.tvWeatherResult.text = "Error: ${e.message}"
                }
            }
        }
    }
}
