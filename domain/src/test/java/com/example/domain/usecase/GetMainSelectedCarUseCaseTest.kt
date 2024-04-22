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


internal class GetMainSelectedCarUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val carRepository: CarRepository = mock()

    private val subject by lazy { GetMainSelectedCarUseCase(carRepository) }

    @Test
    fun `get emits main selected car when carRepository returns car entity`() = runTest {
        whenever(carRepository.getMainSelectedCar()) doReturn flowOf(
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

        subject.get().test {
            verify(carRepository).getMainSelectedCar()
            awaitItem() shouldBe SelectedCar(
                id = 99,
                brand = "audi",
                series = "a1",
                buildYear = 1998,
                features = listOf("LiveData", "BatteryCheck"),
                fuelType = FuelType.Diesel,
                isMain = true
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
