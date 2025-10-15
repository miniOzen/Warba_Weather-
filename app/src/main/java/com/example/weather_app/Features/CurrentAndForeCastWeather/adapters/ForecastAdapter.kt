package com.example.weather_app.Features.CurrentAndForeCastWeather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.Data.remote.Model.ForecastItem
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.ForeCastUiModel
import com.example.weather_app.R
import com.example.weather_app.databinding.LayoutForecastWeatherBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForecastAdapter(
    private val items: List<ForeCastUiModel>,
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(val binding: LayoutForecastWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = LayoutForecastWeatherBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = items[position]

        val timezoneOffsetSeconds = 10800
        val localDate = Date((item.dt + timezoneOffsetSeconds) * 1000L)
        val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(localDate)
        holder.binding.tvDay.text = if (position == 0) "Today" else dayOfWeek

        val minTemp = (item.temp_min - 273.15).toInt()
        val maxTemp = (item.temp_max - 273.15).toInt()

        holder.binding.tvMin.text = "${minTemp}°"
        holder.binding.tvMax.text = "${maxTemp}°"

        when (item.weather.lowercase(Locale.getDefault())) {
            "clear" -> holder.binding.lottieAnim.setAnimation(R.raw.sunny)
            "clouds" -> holder.binding.lottieAnim.setAnimation(R.raw.clouds)
            "rain" -> holder.binding.lottieAnim.setAnimation(R.raw.rainy)
            else -> holder.binding.lottieAnim.setAnimation(R.raw.sunny)
        }

    }

    override fun getItemCount(): Int = items.size
}