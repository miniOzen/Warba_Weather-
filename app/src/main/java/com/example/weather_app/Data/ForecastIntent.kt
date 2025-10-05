package com.example.weather_app.Data

sealed class ForecastIntent {
    object LoadForecast : ForecastIntent()
}