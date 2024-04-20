package com.example.carlydemo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.carlydemo.data.database.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCar(car: CarEntity): Long

    @Query("SELECT * FROM car")
    fun getCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM car WHERE is_selected_as_main = :isMain")
    fun getMainSelectedCar(isMain: Boolean = true): Flow<CarEntity>

    @Query("UPDATE car SET is_selected_as_main = :isMain")
    suspend fun updateAllCarAsNonMain(isMain: Boolean = false)

    @Query(" DELETE FROM car WHERE id = :id")
    suspend fun deleteCar(id: Long?)
}
