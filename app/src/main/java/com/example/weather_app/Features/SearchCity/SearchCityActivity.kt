package com.example.weather_app.Features.SearchCity

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import com.example.weather_app.Features.CurrentAndForeCastWeather.WeatherViewModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.adapters.SearchCityAdapter
import com.example.weather_app.databinding.ActivitySearchCityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCityActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchCityBinding
    val model: SearchViewModel by viewModels()
    val searchItems = mutableListOf<SearchedCityResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = SearchCityAdapter(searchItems, {
            model.saveCity(SavedCityEntity(1, it.name, it.country, it.lon, it.lat))
            onBackPressedDispatcher.onBackPressed()
        })
        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)

        model.searchResult.observe(this){
            if (it.isNotEmpty()){
                searchItems.clear()
                searchItems.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if ((query ?: "").length >= 3) {
                    model.getSearchCity(query ?: "")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if ((newText ?: "").length >= 3) {
                    model.getSearchCity(newText ?: "")
                }
                return true
            }
        })


    }

}