package com.example.data.repository.source

import com.example.data.dto.CarDto

interface OpenCSVManager {
    suspend fun readCarData(): List<CarDto>
}