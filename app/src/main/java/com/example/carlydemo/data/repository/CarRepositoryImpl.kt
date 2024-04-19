package com.example.carlydemo.data.repository

import com.example.carlydemo.data.dto.mapper.toCarList
import com.example.carlydemo.domain.model.Car
import com.example.carlydemo.domain.repository.CarRepository
import java.io.IOException
import javax.inject.Inject

internal class CarRepositoryImpl @Inject constructor(
    private val openCSVManager: OpenCSVManager
) : CarRepository {
    override fun getCars(): List<Car>? {
        return try {
            val cars = openCSVManager.readCarData().toCarList()
            cars
        } catch (e: IOException) {
            null
        }
    }
}
