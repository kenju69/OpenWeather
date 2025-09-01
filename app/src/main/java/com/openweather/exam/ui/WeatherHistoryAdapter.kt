package com.openweather.exam.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openweather.exam.R
import com.openweather.exam.data.WeatherEntity

class WeatherHistoryAdapter(private val items: List<WeatherEntity>) :
    RecyclerView.Adapter<WeatherHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvCityCountry.text = "${item.city}, ${item.country}"
        holder.tvTempDesc.text = "${item.temp.toInt()}°C | ${item.description}"

        // Set icon (sun/moon) based on timestamp (simplified)
        val iconRes = if ((System.currentTimeMillis() / 1000) < item.timestamp) {
            R.drawable.ic_sun
        } else {
            R.drawable.ic_moon
        }
        holder.ivIcon.setImageResource(iconRes)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val tvCityCountry: TextView = view.findViewById(R.id.tvCityCountry)
        val tvTempDesc: TextView = view.findViewById(R.id.tvTempDesc)
    }
}
