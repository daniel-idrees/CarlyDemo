package com.example.carlydemo.data.repository.source

import com.example.carlydemo.data.dto.CarDto

internal interface OpenCSVManager {
    suspend fun readCarData(): List<CarDto>
}