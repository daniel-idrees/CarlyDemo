package com.example.carlydemo.domain.repository

import com.example.carlydemo.domain.model.Car

interface CarRepository {
    fun getCars() : List<Car>?
}