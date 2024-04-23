package com.example.data.repository

import com.example.data.dto.CarDto
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    suspend fun getCars(): Flow<List<CarDto>>
}