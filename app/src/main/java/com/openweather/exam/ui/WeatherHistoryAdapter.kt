package com.openweather.exam.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openweather.exam.R
import com.openweather.exam.data.WeatherEntity

class WeatherHistoryAdapter(
    private var items: List<WeatherEntity> = emptyList()
) : RecyclerView.Adapter<WeatherHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_history, parent, false)
        return HistoryViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvCityCountry.text = "${item.city}, ${item.country}"
        holder.tvTempDesc.text = "${item.temp?.toInt()}°C | ${item.description}"

        val iconRes = when {
            item.description.contains("rain", true) -> R.drawable.ic_rain
            (System.currentTimeMillis() / 1000) < item.sunset!! -> R.drawable.ic_sun
            else -> R.drawable.ic_moon
        }
        val d = holder.ivIcon.context.getDrawable(iconRes)
        val size = (48 * holder.ivIcon.resources.displayMetrics.density).toInt()
        d?.setBounds(0, 0, size, size)
        holder.ivIcon.setImageDrawable(d)
    }

    fun updateData(newItems: List<WeatherEntity>) {
        items = newItems
       // items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivIcon: ImageView = v.findViewById(R.id.ivIcon)
        val tvCityCountry: TextView = v.findViewById(R.id.tvCityCountry)
        val tvTempDesc: TextView = v.findViewById(R.id.tvTempDesc)
    }
}
