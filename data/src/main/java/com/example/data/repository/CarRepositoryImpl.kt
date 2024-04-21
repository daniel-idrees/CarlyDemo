package com.example.data.repository

import com.example.data.database.dao.CarDao
import com.example.data.database.entity.CarEntity
import com.example.data.dto.CarDto
import com.example.data.repository.source.OpenCSVManager
import kotlinx.coroutines.flow.Flow
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

internal class CarRepositoryImpl @Inject constructor(
    private val openCSVManager: OpenCSVManager,
    private val dao: CarDao
) : CarRepository {

    override suspend fun getCars(): List<CarDto> {
        return try {
            val cars = openCSVManager.readCarData()
            cars
        } catch (e: IOException) {
            emptyList()
        } catch (e: FileNotFoundException) {
            emptyList()
        }
    }
    override suspend fun getSelectedCars(): Flow<List<CarEntity>> = dao.getCars()

    override fun getMainSelectedCar(): Flow<CarEntity?> = dao.getMainSelectedCar()

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
