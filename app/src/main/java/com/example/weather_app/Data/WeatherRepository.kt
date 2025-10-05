package com.example.weather_app.Data


import com.example.weather_app.Data.Database.SavedCityDao
import com.example.weather_app.Data.Api.*
import com.example.weather_app.Data.Database.SavedCityEntity
import com.example.weather_app.Data.Model.CurrentWeatherResponse
import com.example.weather_app.Data.Model.ForecastWeatherResponse
import com.example.weather_app.Data.Model.SearchedCityResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: ApiInterface,
    private val savedCityDao: SavedCityDao,
) {


    suspend fun getSavedCities(): List<SavedCityEntity> {
        return savedCityDao.getSaveCites()
    }

    suspend fun insertCity(city: SavedCityEntity) {
        savedCityDao.insertCity(city)
    }

    suspend fun deleteCity(city: SavedCityEntity) {
        savedCityDao.deleteCity(city)
    }

    suspend fun getCurrentWeather(queryMap: Map<String, String>): CurrentWeatherResponse {
        return api.getCurrentWeather(queryMap)
    }

    suspend fun getSearchedCity(queryMap: Map<String, String>): SearchedCityResponse {
        return api.getCities(queryMap)
    }

    suspend fun getForecastWeather(queryMap: Map<String, String>): ForecastWeatherResponse {
        return api.getForecastWeather(queryMap)
    }




}
