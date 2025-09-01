package com.openweather.exam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.BuildConfig
import com.openweather.exam.R
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.RetrofitInstance
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.data.WeatherResponse
import com.openweather.exam.databinding.FragmentCurrentWeatherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private val repo by lazy {
        val db = AppDatabase.getDatabase(requireContext())
        WeatherRepository(
            db.userDao(),
            db.weatherDao(),
            RetrofitInstance.api   // ✅ FIXED: provide apiService
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isNotEmpty()) searchCity(city)
        }

        return binding.root
    }

    private fun searchCity(city: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = repo.fetchWeatherByCity(city, getString(R.string.api_key))
                withContext(Dispatchers.Main) {
                    updateUI(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.tvWeather.text = "Error: ${e.message}"
                    binding.tvSun.text = ""
                    binding.ivIcon.setImageDrawable(null)
                }
            }
        }
    }

    private fun updateUI(resp: WeatherResponse) {
        val city = resp.city ?: "Unknown"
        val country = resp.sys?.country ?: ""
        val temp = resp.main?.temp?.toInt() ?: 0
        val desc = resp.weather?.firstOrNull()?.description ?: "N/A"

        binding.tvWeather.text = "$city, $country\n${temp}°C\n$desc"

        val sunrise = resp.sys?.sunrise ?: 0L
        val sunset = resp.sys?.sunset ?: 0L
        val fmt = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.tvSun.text = "Sunrise: ${fmt.format(Date(sunrise * 1000))} | Sunset: ${fmt.format(Date(sunset * 1000))}"

        val iconRes = when {
            desc.contains("rain", true) -> R.drawable.ic_rain
            (System.currentTimeMillis() / 1000) < sunset -> R.drawable.ic_sun
            else -> R.drawable.ic_moon
        }
        val d = resources.getDrawable(iconRes, null)
        val size = (48 * resources.displayMetrics.density).toInt()
        d.setBounds(0, 0, size, size)
        binding.ivIcon.setImageDrawable(d)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
