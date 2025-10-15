package com.example.weather_app.Data.remote

import com.example.weather_app.Data.remote.Model.CurrentWeatherResponse
import com.example.weather_app.Data.remote.Model.ForecastWeatherResponse
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("/geo/1.0/direct")
    suspend fun getCities(@QueryMap map: Map<String, String>): List<SearchedCityResponse>

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(@QueryMap map: Map<String, String>) : CurrentWeatherResponse

    @GET("/data/2.5/forecast")
    suspend fun getForecastWeather(@QueryMap map: Map<String, String>) : ForecastWeatherResponse


}