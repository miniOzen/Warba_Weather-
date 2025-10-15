package com.example.weather_app.Features.CurrentAndForeCastWeather

import com.example.weather_app.Data.remote.Model.ForecastWeatherResponse
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.ForeCastUiModel

data class ForecastState(
    val isLoading: Boolean = false,
    val forecast: List<ForeCastUiModel>? = null,
    val error: String? = null
)