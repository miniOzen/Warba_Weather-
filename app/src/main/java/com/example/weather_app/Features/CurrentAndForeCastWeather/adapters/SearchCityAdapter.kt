package com.example.weather_app.Features.CurrentAndForeCastWeather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import com.example.weather_app.databinding.LayoutSearchCityBinding
import java.util.Locale


class SearchCityAdapter(
    private val cities: List<SearchedCityResponse>,
    private val onItemClick: (SearchedCityResponse) -> Unit,
) : RecyclerView.Adapter<SearchCityAdapter.CityViewHolder>() {

    inner class CityViewHolder(private val binding: LayoutSearchCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: SearchedCityResponse) {

            if (layoutPosition == 0) {
                binding.sp1.visibility = View.GONE
            } else {
                binding.sp1.visibility = View.VISIBLE

            }

            binding.tvCityName.text = if (!city.state.isNullOrEmpty()) {
                "${city.name}, ${city.state}"
            } else {
                city.name
            }

            binding.tvCountry.text = getCountryName(city.country)

            binding.root.setOnClickListener {
                onItemClick(city)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = LayoutSearchCityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    fun getCountryName(countryCode: String): String {
        val locale = Locale("", countryCode)
        return locale.displayCountry
    }

    override fun getItemCount(): Int = cities.size
}