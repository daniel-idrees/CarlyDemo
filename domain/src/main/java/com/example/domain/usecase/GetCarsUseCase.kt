package com.example.domain.usecase

import com.example.data.dto.CarDto
import com.example.data.repository.CarRepository
import com.example.domain.model.Car
import com.example.domain.model.mapper.toCar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun get(): Flow<List<Car>> = carRepository.getCars().map { entities ->
        entities.map(CarDto::toCar)
    }
}
