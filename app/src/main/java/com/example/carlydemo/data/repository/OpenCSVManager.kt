package com.example.carlydemo.data.repository

import com.example.carlydemo.data.dto.CarDto

internal interface OpenCSVManager {
    suspend fun readCarData(): List<CarDto>
}
