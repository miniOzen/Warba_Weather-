package com.example.weather_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather_app.Features.WeatherViewModel
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Data.Model.ForecastItem
import com.example.weather_app.Features.SearchCityActivity
import com.example.weather_app.Features.adapters.ForecastAdapter
import com.example.weather_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val model: WeatherViewModel by viewModels()
    val forecastWeather = mutableListOf<ForecastItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ForecastAdapter(forecastWeather)
        binding.rvForecast.adapter = adapter
        binding.rvForecast.layoutManager = LinearLayoutManager(this)

        model.fetchCurrentWeather()

        binding.imgSearch.setOnClickListener {
            val intent = Intent(this, SearchCityActivity::class.java)
            startActivity(intent)
        }

        model.weather.observe(this) {


            binding.lottieAnm.setAnimation(getWeatherIcon(it.weather[0].main, it.weather[0].icon))
            binding.tvCityName.text = it.name
            binding.tvWeather.text = it.weather[0].main
            binding.tvFeel.text =
                getString(R.string.feel_like, formatDegreeToCelsius(it.main.feels_like).toString())
            binding.tvMinMax.text = getString(
                R.string.max_min,
                formatDegreeToCelsius(it.main.temp_max).toString(),
                formatDegreeToCelsius(it.main.temp_min).toString()
            )
            binding.tvHumidity.text = getString(R.string.humidity, it.main.humidity.toString())
            binding.tvWind.text = getString(R.string.wind, it.wind.speed.toString())
            binding.tvWeatherDegree.text = "${formatDegreeToCelsius(it.main.temp)}°"
        }

        model.getForeCast()
        model.forecast.observe(this) {
            if (it.list.isNotEmpty()) {
                forecastWeather.clear()
                forecastWeather.addAll(it.list)
                adapter.notifyDataSetChanged()
            }

        }
    }


    fun formatDegreeToCelsius(kelvin: Double): Int {
        return (kelvin - 273.15).toInt()
    }

    fun getWeatherIcon(weather: String, icon: String): Int {
        return when (weather) {
            "Clouds" -> R.raw.clouds
            "Rain" -> R.raw.rainy
            else -> R.raw.sunny
        }

    }



    override fun onResume() {
        super.onResume()
        model.fetchCurrentWeather()
        model.getForeCast()
    }


}