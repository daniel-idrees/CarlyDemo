package com.example.carlydemo.domain.repository

import com.example.carlydemo.domain.model.Car

interface CarRepository {
    suspend fun getCars() : List<Car>?
}