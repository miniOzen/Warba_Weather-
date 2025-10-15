package com.example.weather_app.Data.remote.Model

import com.example.weather_app.Data.Database.Entities.CurrentWeatherEntity
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.CurrentWeatherUIModel


data class CurrentWeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int,
)

data class Coord(
    val lon: Double,
    val lat: Double,
)


data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?,
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?,
)

data class Clouds(
    val all: Int,
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)

fun CurrentWeatherResponse.toUIModel(calledTime: Long): CurrentWeatherUIModel {
    val weatherIcon = if (weather.isNotEmpty()) weather[0].icon else ""

    return CurrentWeatherUIModel(
        name = name,
        feels_like = main.feels_like,
        speed = wind.speed,
        temp_min = main.temp_min,
        temp_max = main.temp_max,
        temp = main.temp,
        icon = weatherIcon,
        humidity = main.humidity,
        calledTime = calledTime, weather = weather[0].main
    )
}

