package com.openweather.exam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.RetrofitInstance
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.databinding.FragmentHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeatherHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())
        val repo = WeatherRepository(db.userDao(), db.weatherDao(), com.openweather.exam.data.RetrofitInstance.api)

        viewLifecycleOwner.lifecycleScope.launch {
            repo.getWeatherHistory().collectLatest { list ->
                adapter = WeatherHistoryAdapter(list)
                binding.rvHistory.adapter = adapter
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
