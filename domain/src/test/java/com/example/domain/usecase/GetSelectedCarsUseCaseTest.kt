package com.example.domain.usecase

import app.cash.turbine.test
import com.example.data.database.entity.CarEntity
import com.example.data.repository.CarRepository
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.common.MainDispatcherRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


internal class GetSelectedCarsUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val carRepository: CarRepository = mock()

    private val subject by lazy { GetSelectedCarsUseCase(carRepository) }

    @Test
    fun `get emits empty list when carRepository returns empty list`() = runTest {
        whenever(carRepository.getSelectedCars()) doReturn flowOf(emptyList())

        subject.get().test {
            verify(carRepository).getSelectedCars()
            awaitItem() shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get emits list of cars when carRepository returns car entity list`() = runTest {
        whenever(carRepository.getSelectedCars()) doReturn flowOf(
            listOf(
                CarEntity(
                    id = 99,
                    brandName = "audi",
                    seriesName = "a1",
                    buildYear = 1998,
                    supportedFeatures = listOf("LiveData", "BatteryCheck"),
                    fuelType = "Diesel",
                    isSelectedAsMain = true
                ),
                CarEntity(
                    id = 100,
                    brandName = "bmw",
                    seriesName = "x1",
                    buildYear = 2000,
                    supportedFeatures = listOf("LiveData"),
                    fuelType = "Gasoline",
                    isSelectedAsMain = false
                )
            )
        )

        subject.get().test {
            verify(carRepository).getSelectedCars()
            awaitItem() shouldBe listOf(
                SelectedCar(
                    id = 99,
                    brand = "audi",
                    series = "a1",
                    buildYear = 1998,
                    features = listOf("LiveData", "BatteryCheck"),
                    fuelType = FuelType.Diesel,
                    isMain = true
                ),

                SelectedCar(
                    id = 100,
                    brand = "bmw",
                    series = "x1",
                    buildYear = 2000,
                    features = listOf("LiveData"),
                    fuelType = FuelType.Gasoline,
                    isMain = false
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
