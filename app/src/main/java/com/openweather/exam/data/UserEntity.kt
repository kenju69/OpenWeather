package com.openweather.exam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String,
    val passwordHash: String
)
