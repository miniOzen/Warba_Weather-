package com.example.weather_app.Data

import com.example.weather_app.Data.Model.ForecastItem
import com.example.weather_app.Data.Model.ForecastWeatherResponse

data class ForecastState(
    val isLoading: Boolean = false,
    val forecast: ForecastWeatherResponse? = null ,
    val error: String? = null
)