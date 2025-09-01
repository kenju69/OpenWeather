package com.openweather.exam.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity)

    @Query("SELECT * FROM weather_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<WeatherEntity>>
}
