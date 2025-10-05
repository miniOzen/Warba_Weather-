package com.example.weather_app.Data.Api

import com.example.weather_app.Data.Model.CurrentWeatherResponse
import com.example.weather_app.Data.Model.ForecastWeatherResponse
import com.example.weather_app.Data.Model.SearchedCityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("/geo/1.0/direct")
    suspend fun getCities(@QueryMap map: Map<String, String>): SearchedCityResponse

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(@QueryMap map: Map<String, String>) : CurrentWeatherResponse

    @GET("/data/2.5/forecast")
    suspend fun getForecastWeather(@QueryMap map: Map<String, String>) : ForecastWeatherResponse


}