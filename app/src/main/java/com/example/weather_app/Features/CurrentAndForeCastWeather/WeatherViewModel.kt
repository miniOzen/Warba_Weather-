package com.example.weather_app.Features.CurrentAndForeCastWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Features.CurrentAndForeCastWeather.ForecastIntent
import com.example.weather_app.Features.CurrentAndForeCastWeather.ForecastState
import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Data.remote.Model.CurrentWeatherResponse
import com.example.weather_app.Data.remote.Model.ForecastWeatherResponse
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import com.example.weather_app.Data.remote.Model.toUiModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.CurrentWeatherUIModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.ForeCastUiModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.usecase.CurrentWeatherUseCase
import com.example.weather_app.Features.CurrentAndForeCastWeather.usecase.ForeCastUseCase
import com.example.weather_app.Features.common.SaveCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val saveCityUseCase: SaveCityUseCase,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val foreCastUseCase: ForeCastUseCase,
) : ViewModel() {

    private val _weather = MutableLiveData<CurrentWeatherUIModel>()
    val weather: LiveData<CurrentWeatherUIModel> get() = _weather

    private val _searchResult = MutableLiveData<List<SearchedCityResponse>>()
    val searchResult: LiveData<List<SearchedCityResponse>> get() = _searchResult

    private val _state = MutableStateFlow(ForecastState())
    val state: StateFlow<ForecastState> get() = _state

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun processIntent(intent: ForecastIntent) {
        when (intent) {
            is ForecastIntent.LoadForecast -> getForeCast()
        }
    }



    fun fetchCurrentWeather() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = currentWeatherUseCase.invoke()
                _weather.value = response

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun getForeCast() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = foreCastUseCase.invoke()
            result.onSuccess { forecast ->
                val response = forecast as? ForecastWeatherResponse
                val currentForecast = response?.list?.map { it.toUiModel() } ?: emptyList()
                _state.update { it.copy(forecast = currentForecast, isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun saveCity(city: SavedCityEntity) {
        viewModelScope.launch {
            saveCityUseCase.invoke(city)
        }
    }


}