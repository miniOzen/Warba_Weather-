package com.example.weather_app.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather_app.Data.Database.SavedCityEntity

@Database(entities = [SavedCityEntity::class], version = 1 , exportSchema = false)
abstract class SavedCityDB : RoomDatabase() {
    abstract fun savedCityDao(): SavedCityDao
}

