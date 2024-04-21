package com.example.domain.usecase

import com.example.domain.repository.CarRepository
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun get() = flowOf(carRepository.getCars())
}
