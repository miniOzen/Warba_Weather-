package com.example.weather_app.Features.SearchCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import com.example.weather_app.Features.CurrentAndForeCastWeather.usecase.CurrentWeatherUseCase
import com.example.weather_app.Features.CurrentAndForeCastWeather.usecase.ForeCastUseCase
import com.example.weather_app.Features.common.SaveCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val saveCityUseCase: SaveCityUseCase,
    private val searchCityUseCase: SearchCityUseCase,
) : ViewModel() {
    private val _searchResult = MutableLiveData<List<SearchedCityResponse>>()
    val searchResult: LiveData<List<SearchedCityResponse>> get() = _searchResult
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun getSearchCity(city: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val searchResult = searchCityUseCase.invoke(city)
                _searchResult.value = searchResult

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveCity(city: SavedCityEntity) {
        viewModelScope.launch {
            saveCityUseCase.invoke(city)
        }
    }

}