package com.example.weather_app.Data.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedCity")
data class SavedCityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val countryCode: String,
    val lon: Double,
    val lat: Double,
    val state: String,
)