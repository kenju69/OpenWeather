package com.openweather.exam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: WeatherHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val repo = com.openweather.exam.data.WeatherRepository(db.userDao(), db.weatherDao())

        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repo.getWeatherHistory().collectLatest { history ->
                adapter = WeatherHistoryAdapter(history)
                binding.rvHistory.adapter = adapter
            }
        }
    }
}
