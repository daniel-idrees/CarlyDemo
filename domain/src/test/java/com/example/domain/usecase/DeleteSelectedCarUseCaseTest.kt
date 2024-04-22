package com.example.domain.usecase

import com.example.data.database.entity.CarEntity
import com.example.data.repository.CarRepository
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.common.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class DeleteSelectedCarUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val carRepository: CarRepository = mock()

    private val subject by lazy { DeleteSelectedCarUseCase(carRepository) }

    @Test
    fun `delete should provide the repository with correct car entity of selected car`() = runTest {

        subject.delete(
            SelectedCar(
                id = 99,
                brand = "audi",
                series = "a1",
                buildYear = 1998,
                features = listOf("LiveData", "BatteryCheck"),
                fuelType = FuelType.Diesel,
                isMain = true
            )
        )

        verify(carRepository).deleteSelectedCar(
            CarEntity(
                id = 99,
                brandName = "audi",
                seriesName = "a1",
                buildYear = 1998,
                supportedFeatures = listOf("LiveData", "BatteryCheck"),
                fuelType = "Diesel",
                isSelectedAsMain = true
            )
        )
    }
}