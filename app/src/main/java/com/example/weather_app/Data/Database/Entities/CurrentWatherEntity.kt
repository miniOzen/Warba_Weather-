package com.example.weather_app.Data.Database.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.CurrentWeatherUIModel

@Entity(tableName = "CurrentWeather")
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val feels_like: Double,
    val speed: Double,
    val temp_min: Double,
    val calledTime: Long,
    val temp_max: Double,
    val temp: Double,
    val icon: String,
    val humidity: Int,
    val weather: String, val lon: Double, val lat: Double,
)

fun CurrentWeatherEntity.toUIModel(): CurrentWeatherUIModel {
    return CurrentWeatherUIModel(
        name,
        feels_like,
        speed,
        temp_min,
        calledTime,
        temp_max,
        temp,
        icon,
        humidity,
        weather
    )
}

