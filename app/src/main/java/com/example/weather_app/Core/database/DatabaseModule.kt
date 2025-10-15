package com.example.weather_app.Core.database

import android.content.Context
import androidx.room.Room
import com.example.weather_app.Data.Database.Dao.CurrentWeatherDao
import com.example.weather_app.Data.Database.Dao.SavedCityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherAppDB {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherAppDB::class.java,
            "weather_app_dp"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: WeatherAppDB): SavedCityDao {
        return db.savedCityDao()
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(db: WeatherAppDB): CurrentWeatherDao {
        return db.currentWeatherDao()
    }
}