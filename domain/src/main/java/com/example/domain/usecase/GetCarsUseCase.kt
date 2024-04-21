package com.example.domain.usecase

import com.example.data.dto.CarDto
import com.example.data.repository.CarRepository
import com.example.domain.model.mapper.toCar
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun get() = flowOf(carRepository.getCars().map(CarDto::toCar))
}
