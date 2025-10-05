package com.example.weather_app.Data.Database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedCityDBModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SavedCityDB {
        return Room.databaseBuilder(
            context.applicationContext,
            SavedCityDB::class.java,
            "saved_city_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: SavedCityDB): SavedCityDao {
        return db.savedCityDao()
    }
}