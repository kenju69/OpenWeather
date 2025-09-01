package com.openweather.exam.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.databinding.ActivityHistoryBinding
import com.openweather.exam.viewmodel.HistoryViewModel
import com.openweather.exam.viewmodel.HistoryViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private val viewModel: HistoryViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        HistoryViewModelFactory(WeatherRepository(db.userDao(), db.weatherDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.lvHistory.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.getHistory().collectLatest { history ->
                adapter.clear()
                adapter.addAll(history.map { "${it.city}, ${it.country} - ${it.temp}°C" })
            }
        }
    }
}
