package com.example.data.repository

import com.example.data.dto.CarDto
import com.example.data.repository.source.OpenCSVManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CarCSVRepository @Inject constructor(
    private val openCSVManager: OpenCSVManager
) : CarRepository {

    override suspend fun getCars(): Flow<List<CarDto>> = openCSVManager.readCarData()
}
