package com.example.ui.carselection

import app.cash.turbine.test
import com.example.domain.model.Car
import com.example.domain.usecase.AddSelectedCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import com.example.ui.common.MainDispatcherRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class CarSelectionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val fakeCars = listOf(
        Car(
            brand = "bmw",
            series = "x1",
            minSupportedYear = 1990,
            maxSupportedYear = 1998,
            features = emptyList()
        ),
        Car(
            brand = "audi",
            series = "a1",
            minSupportedYear = 1990,
            maxSupportedYear = 1998,
            features = emptyList()
        ),
        Car(
            brand = "audi",
            series = "a2",
            minSupportedYear = 1990,
            maxSupportedYear = 1998,
            features = emptyList()
        )
    )

    private val getCarsUseCase: GetCarsUseCase = mock {
        onBlocking { it.get() } doReturn flowOf(fakeCars)
    }

    private val addSelectedCarUseCase: AddSelectedCarUseCase = mock()

    private val subject by lazy {
        CarSelectionViewModel(
            getCarsUseCase,
            addSelectedCarUseCase,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `view state should be updated on selection actions`() = runTest {
        val brandToSelect = "audi"
        val seriesToSelect = "a1"
        val modelYearToSelect = "1998"
        val fuelTypeToSelect = "Diesel"

        val brands = fakeCars.map { it.brand }.distinct()
        val series = fakeCars.filter { it.brand == brandToSelect }.map { it.series }

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brands)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandToSelect))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, series)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesToSelect))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                1990,
                1998
            )


            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect
            )

            //after fuel type selection
            subject.onAction(CarSelectionAction.OnFuelTypeSelected(fuelTypeToSelect))
            awaitItem() shouldBe CarSelectionUiState.CarSelectionFinished

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `view state should be updated on up press action`() = runTest {
        val brandToSelect = "audi"
        val seriesToSelect = "a1"
        val modelYearToSelect = "1998"

        val brands = fakeCars.map { it.brand }.distinct()
        val series = fakeCars.filter { it.brand == brandToSelect }.map { it.series }

        subject.viewState.test {

            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brands)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandToSelect))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, series)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesToSelect))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                1990,
                1998
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            val stateAfterBuildYearSelection = awaitItem()
            stateAfterBuildYearSelection shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect
            )

            // up press on fuel type selection state
            subject.onAction(CarSelectionAction.UpPressed(stateAfterBuildYearSelection))
            val stateAfterBackPressed = awaitItem()
            stateAfterBackPressed shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                1990,
                1998
            )

            // up press on build year selection
            subject.onAction(CarSelectionAction.UpPressed(stateAfterBackPressed))
            val stateAfterBackPressedOnBuildYearSelection = awaitItem()
            stateAfterBackPressedOnBuildYearSelection shouldBe CarSelectionUiState.SeriesSelection(
                brandToSelect,
                series
            )

            // up press on series selection
            subject.onAction(CarSelectionAction.UpPressed(stateAfterBackPressedOnBuildYearSelection))
            val stateAfterBackPressedOnSeriesSelection = awaitItem()
            stateAfterBackPressedOnSeriesSelection shouldBe CarSelectionUiState.BrandSelection(
                brands
            )

            // up press on brand selection
            subject.events.test {
                subject.onAction(CarSelectionAction.UpPressed(stateAfterBackPressedOnSeriesSelection))
                awaitItem() shouldBe CarSelectionUiEvent.GoBack
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `view state should be updated on back press action `() = runTest {
        val brandToSelect = "audi"
        val seriesToSelect = "a1"
        val modelYearToSelect = "1998"

        val brands = fakeCars.map { it.brand }.distinct()
        val series = fakeCars.filter { it.brand == brandToSelect }.map { it.series }

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brands)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandToSelect))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, series)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesToSelect))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                1990,
                1998
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            val stateAfterBuildYearSelection = awaitItem()
            stateAfterBuildYearSelection shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect
            )

            // back press on fuel type selection state
            subject.onAction(CarSelectionAction.OnBackPressed(stateAfterBuildYearSelection))
            val stateAfterBackPressed = awaitItem()
            stateAfterBackPressed shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                1990,
                1998
            )

            // back press on build year selection
            subject.onAction(CarSelectionAction.OnBackPressed(stateAfterBackPressed))
            val stateAfterBackPressedOnBuildYearSelection = awaitItem()
            stateAfterBackPressedOnBuildYearSelection shouldBe CarSelectionUiState.SeriesSelection(
                brandToSelect,
                series
            )

            // back press on series selection
            subject.onAction(
                CarSelectionAction.OnBackPressed(
                    stateAfterBackPressedOnBuildYearSelection
                )
            )
            val stateAfterBackPressedOnSeriesSelection = awaitItem()
            stateAfterBackPressedOnSeriesSelection shouldBe CarSelectionUiState.BrandSelection(
                brands
            )

            // back press on brand selection
            subject.events.test {
                subject.onAction(
                    CarSelectionAction.OnBackPressed(
                        stateAfterBackPressedOnSeriesSelection
                    )
                )
                awaitItem() shouldBe CarSelectionUiEvent.GoBack
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search icon click action should invoke navigate to search ui event `() = runTest {
        subject.events.test {
            subject.onAction(CarSelectionAction.SearchIconClicked)
            awaitItem() shouldBe CarSelectionUiEvent.NavigateToSearch
        }
    }
}
