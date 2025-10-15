package com.example.weather_app.Data.remote.Model

import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.ForeCastUiModel


data class ForecastWeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: City,
)

data class ForecastItem(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: ForecastSys,
    val dt_txt: String,
)

data class ForecastMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double,
)

data class ForecastSys(
    val pod: String,
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long,
)

fun ForecastItem.toUiModel(): ForeCastUiModel {
    return ForeCastUiModel(
        this.weather[0].main,
        this.main.temp_min,
        this.main.temp_max,
        this.dt
    )
}


