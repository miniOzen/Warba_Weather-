package com.example.weather_app.Core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_app.Data.Database.Dao.CurrentWeatherDao
import com.example.weather_app.Data.Database.Dao.SavedCityDao
import com.example.weather_app.Data.Database.Entities.CurrentWeatherEntity
import com.example.weather_app.Data.Database.Entities.SavedCityEntity

@Database(entities = [SavedCityEntity::class, CurrentWeatherEntity::class], version = 3 , exportSchema = false)
abstract class WeatherAppDB : RoomDatabase() {
    abstract fun savedCityDao(): SavedCityDao
    abstract fun currentWeatherDao(): CurrentWeatherDao
}