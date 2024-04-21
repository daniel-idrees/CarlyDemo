package com.example.data.repository.source

import com.example.data.dto.CarDto
import kotlinx.coroutines.flow.Flow

internal interface OpenCSVManager {
    suspend fun readCarData(): Flow<List<CarDto>>
}