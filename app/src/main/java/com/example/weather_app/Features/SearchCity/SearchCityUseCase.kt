package com.example.weather_app.Features.SearchCity

import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(city: String): List<SearchedCityResponse> {
        val map = mapOf(
            "q" to city, "limit" to "5", "appid" to "e25592eec79bd71833abe64ac5141d63"

        )
        val response = repository.getSearchedCity(map)
        return response
    }
}