package com.example.weather_app.Features.CurrentAndForeCastWeather

sealed class ForecastIntent {
    object LoadForecast : ForecastIntent()
}