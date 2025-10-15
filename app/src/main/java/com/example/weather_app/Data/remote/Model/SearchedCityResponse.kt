package com.example.weather_app.Data.remote.Model

data class SearchedCityResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?,
)