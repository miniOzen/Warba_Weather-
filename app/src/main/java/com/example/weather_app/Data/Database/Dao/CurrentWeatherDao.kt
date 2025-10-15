package com.example.weather_app.Data.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.Data.Database.Entities.CurrentWeatherEntity

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * From currentweather WHERE id == :weatherId LIMIT 1  ")
    suspend fun getCurrentWeather(weatherId:Int): CurrentWeatherEntity?


    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCurrentWeather(city: CurrentWeatherEntity)
}