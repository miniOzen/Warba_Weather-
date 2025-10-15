package com.example.weather_app.Features.CurrentAndForeCastWeather.usecase

import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.Database.Entities.toUIModel
import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Data.remote.Model.toUIModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.CurrentWeatherUIModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.toEntity
import javax.inject.Inject

class CurrentWeatherUseCase @Inject constructor(val repository: WeatherRepository) {

    suspend operator fun invoke(): CurrentWeatherUIModel {
        val cities = repository.getSavedCities()
        val savedCity = cities.getOrNull(0)
        val duration = 60 * 1000L
        val now = System.currentTimeMillis()
        val localCurrentWeather = repository.getLocalCurrentWeather()
        if (localCurrentWeather != null && (now - localCurrentWeather.calledTime) < duration && savedCity?.lat.toString()
                .contains(localCurrentWeather.lat.toString().substring(1,4)) && savedCity?.lon.toString()
                .contains(localCurrentWeather.lon.toString().substring(1,4))
        ) {
            return localCurrentWeather.toUIModel()
        } else {
            val map = mutableMapOf(
                "appid" to "e25592eec79bd71833abe64ac5141d63",
            )
            if (savedCity != null) {
                map.put("lat", savedCity.lat.toString())
                map.put("lon", savedCity.lon.toString())

            } else {
                map.put("q", "kuwait")
            }
            val response = repository.getCurrentWeather(map)
            val uiModel = response.toUIModel(System.currentTimeMillis())
            repository.insertCurrentWeather(
                uiModel.toEntity(
                    1,
                    response.coord.lon,
                    response.coord.lat
                )
            )
            return uiModel

        }

    }

}