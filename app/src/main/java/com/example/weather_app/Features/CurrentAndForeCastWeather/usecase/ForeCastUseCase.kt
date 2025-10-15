package com.example.weather_app.Features.CurrentAndForeCastWeather.usecase

import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Data.remote.Model.ForecastWeatherResponse
import javax.inject.Inject

class ForeCastUseCase @Inject constructor(val repository: WeatherRepository) {

    suspend operator fun invoke(): Result<Any> {
        return try {
            val cities = repository.getSavedCities()
            val savedCity = cities.firstOrNull()

            val map = mutableMapOf(
                "cnt" to "7",
                "appid" to "e25592eec79bd71833abe64ac5141d63"
            )

            if (savedCity != null) {
                map["lat"] = savedCity.lat.toString()
                map["lon"] = savedCity.lon.toString()
            } else {
                map["q"] = "kuwait"
            }

            val response = repository.getForecastWeather(map)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}