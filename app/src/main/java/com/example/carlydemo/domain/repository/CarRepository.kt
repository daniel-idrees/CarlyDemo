package com.example.carlydemo.domain.repository

import com.example.carlydemo.domain.model.Car
import com.example.carlydemo.domain.model.SelectedCar
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    suspend fun getCars(): List<Car>?
    suspend fun getSelectedCars(): Flow<List<SelectedCar>>
    suspend fun getMainSelectedCar(): Flow<SelectedCar>

    suspend fun addSelectedCar(selectedCar: SelectedCar)
    suspend fun deleteSelectedCar(selectedCar: SelectedCar)
}