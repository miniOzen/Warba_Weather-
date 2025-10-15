package com.example.weather_app.Features.common

import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import javax.inject.Inject

class SaveCityUseCase  @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(city: SavedCityEntity) {
        repository.saveCity(city)
    }
}