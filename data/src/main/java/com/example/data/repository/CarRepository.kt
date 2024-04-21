package com.example.data.repository

import com.example.data.database.entity.CarEntity
import com.example.data.dto.CarDto
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    suspend fun getCars(): List<CarDto>
    suspend fun getSelectedCars(): Flow<List<CarEntity>>
    fun getMainSelectedCar(): Flow<CarEntity?>

    suspend fun addSelectedCar(car: CarEntity)
    suspend fun deleteSelectedCar(car: CarEntity)
    suspend fun setCarAsMain(id: Long?)
}