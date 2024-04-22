package com.example.data.repository

import com.example.data.database.dao.CarDao
import com.example.data.database.entity.CarEntity
import com.example.data.dto.CarDto
import com.example.data.repository.source.OpenCSVManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

internal class CarRepositoryImpl @Inject constructor(
    private val openCSVManager: OpenCSVManager,
    private val dao: CarDao
) : CarRepository {

    override suspend fun getCars(): Flow<List<CarDto>> = openCSVManager.readCarData()

    override fun getSelectedCars(): Flow<List<CarEntity>> = dao.getCars()

    override fun getMainSelectedCar(): Flow<CarEntity?> = dao.getMainSelectedCar().map { it.firstOrNull() }

    override suspend fun addSelectedCar(car: CarEntity) {
        dao.updateAllCarAsNonMain()
        dao.insertCar(car)
    }

    override suspend fun deleteSelectedCar(car: CarEntity) {
        dao.deleteCar(car.id)
    }

    override suspend fun setCarAsMain(id: Long?) {
        dao.updateAllCarAsNonMain()
        dao.updateCarAsMain(id)
    }
}
