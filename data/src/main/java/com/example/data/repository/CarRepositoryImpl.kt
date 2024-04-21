package com.example.data.repository

import com.example.data.database.dao.CarDao
import com.example.data.database.entity.CarEntity
import com.example.data.database.entity.mapper.asEntity
import com.example.data.database.entity.mapper.toSelectedCar
import com.example.data.dto.CarDto
import com.example.data.dto.mapper.toCar
import com.example.data.repository.source.OpenCSVManager
import com.example.domain.model.Car
import com.example.domain.model.SelectedCar
import com.example.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

internal class CarRepositoryImpl @Inject constructor(
    private val openCSVManager: OpenCSVManager,
    private val dao: CarDao
) : CarRepository {

    override suspend fun getCars(): List<Car>? {
        return try {
            val cars = openCSVManager.readCarData().map(CarDto::toCar)
            cars
        } catch (e: IOException) {
            null
        }
    }

    override suspend fun getSelectedCars(): Flow<List<SelectedCar>> {
        val cars = dao.getCars()

        return cars.map { entities -> entities.map(CarEntity::toSelectedCar) }
    }

    override suspend fun getMainSelectedCar(): Flow<SelectedCar> =
        dao.getMainSelectedCar().map { it.toSelectedCar() }

    override suspend fun addSelectedCar(selectedCar: SelectedCar) {
        dao.updateAllCarAsNonMain()
        dao.insertCar(selectedCar.asEntity())
    }

    override suspend fun deleteSelectedCar(selectedCar: SelectedCar) {
        dao.deleteCar(selectedCar.id)
    }
}
