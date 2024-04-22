package com.example.domain.usecase

import app.cash.turbine.test
import com.example.data.dto.CarDto
import com.example.data.repository.CarRepository
import com.example.domain.model.Car
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

internal class GetCarsUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val carRepository: CarRepository = mock()

    private val subject by lazy { GetCarsUseCase(carRepository) }

    @Test
    fun `get should emit list of car when repository returns list of car dtos`() = runTest {
        whenever(carRepository.getCars()) doReturn flowOf(
            listOf(
                CarDto(
                    brandName = "audi",
                    seriesName = "a1",
                    minimumSupportedYear = 1998,
                    maximumSupportedYear = 2010,
                    supportedFeatures = listOf("battery", "livedata")
                ),
                CarDto(
                    brandName = "bmw",
                    seriesName = "x1",
                    minimumSupportedYear = 2000,
                    maximumSupportedYear = 2023,
                    supportedFeatures = listOf("battery")
                )
            )
        )
        subject.get().test {
            verify(carRepository).getCars()
            awaitItem() shouldBe listOf(
                Car(
                    brand = "audi",
                    series = "a1",
                    minSupportedYear = 1998,
                    maxSupportedYear = 2010,
                    features = listOf("battery", "livedata")
                ), Car(
                    brand = "bmw",
                    series = "x1",
                    minSupportedYear = 2000,
                    maxSupportedYear = 2023,
                    features = listOf("battery")
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}