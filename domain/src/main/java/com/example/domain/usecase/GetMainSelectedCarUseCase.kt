package com.example.domain.usecase

import com.example.data.repository.CarRepository
import com.example.domain.model.mapper.toSelectedCar
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMainSelectedCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun get() = carRepository.getMainSelectedCar().map{entity ->
        entity?.toSelectedCar()
    }
}