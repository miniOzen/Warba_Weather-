package com.example.weather_app.Data.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.Data.Database.Entities.SavedCityEntity

@Dao
interface SavedCityDao {
    @Query("SELECT * From savedCity")
   suspend fun getSaveCites(): List<SavedCityEntity>

    @Delete
  suspend  fun deleteCity(city: SavedCityEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCity(city: SavedCityEntity)
}