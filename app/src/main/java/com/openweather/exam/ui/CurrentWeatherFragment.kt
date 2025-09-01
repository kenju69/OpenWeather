package com.openweather.exam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.databinding.FragmentCurrentWeatherBinding
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.WeatherRepository
import kotlinx.coroutines.launch

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: WeatherRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        val db = AppDatabase.getDatabase(requireContext())
        repo = WeatherRepository(db.userDao(), db.weatherDao())

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString()
            fetchWeather(city)
        }

        return binding.root
    }

    private fun fetchWeather(city: String) {
        lifecycleScope.launch {
            try {
                val apiKey = "YOUR_API_KEY"
                val response = repo.fetchWeatherByCity(city, apiKey) // You can add this helper in repo
                binding.tvWeatherResult.text =
                    "${response.city}, ${response.sys.country}\n${response.main.temp.toInt()}°C\n${response.weather.firstOrNull()?.description ?: "N/A"}"
                // TODO: set dynamic icon here (sun/moon/rain)
            } catch (e: Exception) {
                binding.tvWeatherResult.text = "Error: ${e.message}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
