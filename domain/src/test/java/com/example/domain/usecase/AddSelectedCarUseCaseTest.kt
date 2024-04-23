package com.example.domain.usecase

import com.example.data.database.entity.CarEntity
import com.example.data.repository.SelectedCarRepository
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.common.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


internal class AddSelectedCarUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: SelectedCarRepository = mock()

    private val subject by lazy { AddSelectedCarUseCase(repository) }

    @Test
    fun `add should provide the repository with correct car entity of selected car`() = runTest {

        subject.add(
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

        verify(repository).addSelectedCar(
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
