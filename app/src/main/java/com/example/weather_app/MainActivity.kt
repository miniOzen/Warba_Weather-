package com.example.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather_app.Features.WeatherViewModel
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Features.adapters.ForecastAdapter
import com.example.weather_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val model: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.fetchCurrentWeather("london")

        model.weather.observe(this) {


            binding.lottieAnm.setAnimation(getWeatherIcon(it.weather[0].main,it.weather[0].icon))
            binding.tvCityName.text = getCountryName(it.sys.country)
            binding.tvWeather.text = it.weather[0].main
            binding.tvFeel.text = getString(R.string.feel_like,formatDegreeToCelsius(it.main.feels_like).toString())
            binding.tvMinMax.text = getString(R.string.max_min,formatDegreeToCelsius(it.main.temp_max).toString(),formatDegreeToCelsius(it.main.temp_min).toString())
            binding.tvHumidity.text = getString(R.string.humidity,it.main.humidity.toString())
            binding.tvWind.text = getString(R.string.wind,it.wind.speed.toString())
            binding.tvWeatherDegree.text ="${formatDegreeToCelsius(it.main.temp)}°"
        }

        model.getForeCast("")
        model.forecast.observe(this){
            val adapter = ForecastAdapter(it.list)
            binding.rvForecast.adapter = adapter
            binding.rvForecast.layoutManager = LinearLayoutManager(this)
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

    fun getCountryName(countryCode: String): String {
        val locale = Locale("", countryCode)
        return locale.displayCountry
    }


}