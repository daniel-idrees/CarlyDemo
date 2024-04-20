package com.example.carlydemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.carlydemo.data.database.dao.CarDao
import com.example.carlydemo.data.database.entity.CarEntity

@Database(entities = [CarEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}