package com.example.weather_app.Data


import com.example.weather_app.Data.Database.Dao.CurrentWeatherDao
import com.example.weather_app.Data.Database.Dao.SavedCityDao
import com.example.weather_app.Data.Database.Entities.CurrentWeatherEntity
import com.example.weather_app.Data.remote.*
import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.Database.Entities.toUIModel
import com.example.weather_app.Data.remote.Model.CurrentWeatherResponse
import com.example.weather_app.Data.remote.Model.ForecastWeatherResponse
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import com.example.weather_app.Data.remote.Model.toUIModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.CurrentWeatherUIModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: ApiInterface,
    private val savedCityDao: SavedCityDao,
    private val currentWeatherDao: CurrentWeatherDao,
) {


    suspend fun getSavedCities(): List<SavedCityEntity> {
        return savedCityDao.getSaveCites()
    }

    suspend fun saveCity(city: SavedCityEntity) {
        savedCityDao.insertCity(city)
    }



    suspend fun getLocalCurrentWeather(): CurrentWeatherEntity?{
        return currentWeatherDao.getCurrentWeather(1)
    }

    suspend fun insertCurrentWeather(currentWeather: CurrentWeatherEntity){
        currentWeatherDao.insertCurrentWeather(currentWeather)
    }

    suspend fun getCurrentWeather(queryMap: Map<String, String>): CurrentWeatherResponse {
        return api.getCurrentWeather(queryMap)
    }

    suspend fun getSearchedCity(queryMap: Map<String, String>): List<SearchedCityResponse> {
        return api.getCities(queryMap)
    }

    suspend fun getForecastWeather(queryMap: Map<String, String>): ForecastWeatherResponse {
        return api.getForecastWeather(queryMap)
    }


}
