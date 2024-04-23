package com.example.data.repository

import com.example.data.database.entity.CarEntity
import kotlinx.coroutines.flow.Flow

interface SelectedCarRepository {
    fun getSelectedCars(): Flow<List<CarEntity>>
    fun getMainSelectedCar(): Flow<CarEntity?>
    suspend fun addSelectedCar(car: CarEntity)
    suspend fun deleteSelectedCar(car: CarEntity)
    suspend fun setCarAsMain(id: Long?)
}
