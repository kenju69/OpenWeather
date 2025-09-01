package com.openweather.exam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.data.WeatherApiService
import com.openweather.exam.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val adapter = WeatherHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)

        // ✅ Create Retrofit instance here (since you don’t have NetworkModule)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/") // base URL
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val apiService = retrofit.create(WeatherApiService::class.java)

        val repo = WeatherRepository(db.userDao(), db.weatherDao(), apiService)

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter

        lifecycleScope.launch {
            repo.getWeatherHistory().collectLatest { history ->
                adapter.updateData(history)
            }
        }
    }
}
