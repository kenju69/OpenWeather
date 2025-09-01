package com.openweather.exam.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.openweather.exam.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val historyList = mutableListOf("Manila", "Cebu", "Davao")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historyList)
        binding.lvHistory.adapter = adapter
    }
}
