package com.example.weather_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.weather_app.Features.CurrentAndForeCastWeather.WeatherViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Features.CurrentAndForeCastWeather.ForecastIntent
import com.example.weather_app.Data.remote.Model.ForecastItem
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.ForeCastUiModel
import com.example.weather_app.Features.SearchCity.SearchCityActivity
import com.example.weather_app.Features.CurrentAndForeCastWeather.adapters.ForecastAdapter
import com.example.weather_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val model: WeatherViewModel by viewModels()
    val forecastWeather = mutableListOf<ForeCastUiModel>()

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

        model.loading.observe(this){
            if (it== true){
                binding.progDetails.visibility = View.VISIBLE
                binding.layoutWeatherDetails.visibility = View.GONE
            }else{
                binding.progDetails.visibility = View.GONE
                binding.layoutWeatherDetails.visibility = View.VISIBLE
            }
        }

        model.weather.observe(this) {

            binding.lottieAnm.setAnimation(getWeatherIcon(it.weather))
            binding.tvCityName.text = it.name
            binding.tvWeather.text = it.weather
            binding.tvFeel.text =
                getString(R.string.feel_like, formatDegreeToCelsius(it.feels_like).toString())
            binding.tvMinMax.text = getString(
                R.string.max_min,
                formatDegreeToCelsius(it.temp_max).toString(),
                formatDegreeToCelsius(it.temp_min).toString()
            )
            binding.tvHumidity.text = getString(R.string.humidity, it.humidity.toString())
            binding.tvWind.text = getString(R.string.wind, it.speed.toString())
            binding.tvWeatherDegree.text = "${formatDegreeToCelsius(it.temp)}°"
        }

        model.getForeCast()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.getForeCast()
                model.state.collect { state ->


                    state.error?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }

                    if (state.forecast != null && state.forecast.isNotEmpty()) {
                        forecastWeather.clear()
                        forecastWeather.addAll(state.forecast)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        model.processIntent(ForecastIntent.LoadForecast)

    }


    fun formatDegreeToCelsius(kelvin: Double): Int {
        return (kelvin - 273.15).toInt()
    }

    fun getWeatherIcon(weather: String): Int {
        return when (weather) {
            "Clouds" -> R.raw.clouds
            "Rain" -> R.raw.rainy
            else -> R.raw.sunny
        }

    }


    override fun onResume() {
        super.onResume()
        model.fetchCurrentWeather()
    }


}