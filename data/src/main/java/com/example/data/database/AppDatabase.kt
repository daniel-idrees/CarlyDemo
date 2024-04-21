package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.CarDao
import com.example.data.database.entity.CarEntity

@Database(entities = [CarEntity::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}