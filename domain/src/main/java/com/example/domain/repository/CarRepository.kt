package com.example.domain.repository

import com.example.domain.model.Car
import com.example.domain.model.SelectedCar
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    suspend fun getCars(): List<Car>?
    suspend fun getSelectedCars(): Flow<List<SelectedCar>>
    suspend fun getMainSelectedCar(): Flow<SelectedCar>

    suspend fun addSelectedCar(selectedCar: SelectedCar)
    suspend fun deleteSelectedCar(selectedCar: SelectedCar)
}