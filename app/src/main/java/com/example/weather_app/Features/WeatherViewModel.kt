package com.example.weather_app.Features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather_app.Data.Model.CurrentWeatherResponse
import com.example.weather_app.Data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.weather_app.Data.Model.ForecastWeatherResponse
import kotlinx.coroutines.launch


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableLiveData<CurrentWeatherResponse>()
    val weather: LiveData<CurrentWeatherResponse> get() = _weather

    private val _forecast = MutableLiveData<ForecastWeatherResponse>()
    val forecast: LiveData<ForecastWeatherResponse> get() = _forecast

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchCurrentWeather(city: String) {
        viewModelScope.launch {
            _loading.value = false
            try {
                val map  = mapOf(
                    "q" to "salmiya",
                    "appid" to "e25592eec79bd71833abe64ac5141d63",

                )
                val response = repository.getCurrentWeather(map)
                _weather.value = response

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun getForeCast(city: String) {
        viewModelScope.launch {
            _loading.value = false
            try {
                val map  = mapOf(
                    "q" to "salmiya",
                    "cnt" to "7",
                    "appid" to "e25592eec79bd71833abe64ac5141d63"

                    )
                val response = repository.getForecastWeather(map)
                _forecast.value = response

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}