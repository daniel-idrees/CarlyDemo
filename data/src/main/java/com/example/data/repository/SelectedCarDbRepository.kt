package com.example.data.repository

import com.example.data.database.dao.CarDao
import com.example.data.database.entity.CarEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SelectedCarDbRepository @Inject constructor(
    private val dao: CarDao
) : SelectedCarRepository {
    override fun getSelectedCars(): Flow<List<CarEntity>> = dao.getCars()

    override fun getMainSelectedCar(): Flow<CarEntity?> =
        dao.getMainSelectedCar().map { it.firstOrNull() }

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
