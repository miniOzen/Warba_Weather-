package com.example.weather_app.Features.CurrentAndForeCastWeather.Model

data class ForeCastUiModel(
    val weather: String,
    val temp_min: Double,
    val temp_max: Double,
    val dt: Long,
)