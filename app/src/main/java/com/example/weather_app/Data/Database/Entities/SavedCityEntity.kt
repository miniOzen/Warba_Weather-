package com.example.weather_app.Data.Database.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedCity")
data class SavedCityEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val countryCode: String,
    val lon: Double,
    val lat: Double,
)