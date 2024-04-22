package com.example.ui.carlist

import app.cash.turbine.test
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.DeleteSelectedCarUseCase
import com.example.domain.usecase.GetSelectedCarsUseCase
import com.example.domain.usecase.SetCarAsMainUseCase
import com.example.domain.usecase.provider.CarListUseCaseProvider
import com.example.ui.common.MainDispatcherRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class CarListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getSelectedCarsUseCase: GetSelectedCarsUseCase = mock()
    private val deleteSelectedCarUseCase: DeleteSelectedCarUseCase = mock()
    private val setCarAsMainUseCase: SetCarAsMainUseCase = mock()

    private val carListUseCaseProvider: CarListUseCaseProvider = CarListUseCaseProvider(
        getSelectedCarsUseCase,
        deleteSelectedCarUseCase,
        setCarAsMainUseCase
    )

    private val subject by lazy {
        CarListViewModel(
            carListUseCaseProvider,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `view state should be loading initially`() = runTest {
        subject.viewState.value shouldBe CarListUiState.Loading
    }

    @Test
    fun `view state should be selected car state when a car exist in storage`() = runTest {
        val fakeCarList = listOf(
            SelectedCar(
                brand = "BMW",
                series = "3 series",
                buildYear = 2018,
                fuelType = FuelType.Diesel,
                features = emptyList(),
                isMain = true
            ),
            SelectedCar(
                brand = "Audi",
                series = "R",
                buildYear = 2023,
                fuelType = FuelType.Gasoline,
                features = emptyList(),
                isMain = false
            )
        )

        // when
        whenever(carListUseCaseProvider.getSelectedCarsUseCase.get()) doReturn
            flowOf(fakeCarList)

        // then
        subject.viewState.test {
            val result = awaitItem()
            result shouldBe CarListUiState.SelectedCars(fakeCarList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `car item click action should set the car as main`() =
        runTest {
            val anyCar = SelectedCar(
                id = 99,
                brand = "BMW",
                series = "3 series",
                buildYear = 2018,
                fuelType = FuelType.Diesel,
                features = emptyList(),
                isMain = true
            )

            subject.onAction(CarListAction.CarItemClicked(anyCar))
            verify(carListUseCaseProvider.setCarAsMainUseCase).set(99)
        }

    @Test
    fun `delete icon click action should invoke the delete car use case`() =
        runTest {
            val anyCar = SelectedCar(
                id = 99,
                brand = "BMW",
                series = "3 series",
                buildYear = 2018,
                fuelType = FuelType.Diesel,
                features = emptyList(),
                isMain = true
            )

            subject.onAction(CarListAction.DeleteIconClicked(anyCar))
            verify(carListUseCaseProvider.deleteSelectedCarUseCase).delete(anyCar)
        }

    @Test
    fun `Up press action should invoke the go back event`() =
        runTest {
            subject.events.test {
                subject.onAction(CarListAction.UpPressed)
                val result = awaitItem()
                result shouldBe CarListUiEvent.goBack
            }
        }

    @Test
    fun `add button click action should invoke navigation to car selection`() =
        runTest {
            subject.events.test {
                subject.onAction(CarListAction.AddButtonClicked)
                val result = awaitItem()
                result shouldBe CarListUiEvent.NavigateToCarSelection
            }
        }
}
