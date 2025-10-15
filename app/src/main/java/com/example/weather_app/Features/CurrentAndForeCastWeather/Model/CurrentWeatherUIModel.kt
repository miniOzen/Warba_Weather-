package com.example.weather_app.Features.CurrentAndForeCastWeather.Model

import com.example.weather_app.Data.Database.Entities.CurrentWeatherEntity

data class CurrentWeatherUIModel(
    val name: String,
    val feels_like: Double,
    val speed: Double,
    val temp_min: Double,
    val calledTime: Long,
    val temp_max: Double,
    val temp: Double,
    val icon: String,
    val humidity: Int,
    val weather: String,
)

fun CurrentWeatherUIModel.toEntity(id: Int, lon: Double, lat: Double): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        id = id,
        name = this.name,
        feels_like = this.feels_like,
        speed = this.speed,
        temp_min = this.temp_min,
        calledTime = this.calledTime,
        temp_max = this.temp_max,
        temp = this.temp,
        icon = this.icon,
        humidity = this.humidity,
        weather, lat = lat, lon = lon

    )
}