package com.example.ui.carselection

import app.cash.turbine.test
import com.example.domain.model.Car
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
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
import org.mockito.kotlin.verify

internal class CarSelectionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val fakeCars = listOf(
        Car(
            brand = "bmw",
            series = "x1",
            minSupportedYear = 1990,
            maxSupportedYear = 1998,
            features = listOf("Battery Check")
        ),
        Car(
            brand = "audi",
            series = "a1",
            minSupportedYear = 1990,
            maxSupportedYear = 1998,
            features = listOf("Diagnostics")
        ),
        Car(
            brand = "audi",
            series = "a2",
            minSupportedYear = 1990,
            maxSupportedYear = 1998,
            features = listOf("Live Check")
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
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                FuelType.getList()
            )

            //after fuel type selection
            subject.onAction(CarSelectionAction.OnFuelTypeSelected(fuelTypeToSelect))

            subject.events.test {
                awaitItem() shouldBe CarSelectionUiEvent.NavigateToDashboard
            }

            verify(addSelectedCarUseCase).add(
                SelectedCar(
                    brand = "audi",
                    series = "a1",
                    buildYear = 1998,
                    fuelType = FuelType.Diesel,
                    features = listOf("Diagnostics"),
                    isMain = true,
                )
            )

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
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            val stateAfterBuildYearSelection = awaitItem()
            stateAfterBuildYearSelection shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                FuelType.getList()
            )

            // up press on fuel type selection state
            subject.onAction(CarSelectionAction.UpPressed)
            val stateAfterBackPressed = awaitItem()
            stateAfterBackPressed shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            // up press on build year selection
            subject.onAction(CarSelectionAction.UpPressed)
            val stateAfterBackPressedOnBuildYearSelection = awaitItem()
            stateAfterBackPressedOnBuildYearSelection shouldBe CarSelectionUiState.SeriesSelection(
                brandToSelect,
                series
            )

            // up press on series selection
            subject.onAction(CarSelectionAction.UpPressed)
            val stateAfterBackPressedOnSeriesSelection = awaitItem()
            stateAfterBackPressedOnSeriesSelection shouldBe CarSelectionUiState.BrandSelection(
                brands
            )

            // up press on brand selection
            subject.events.test {
                subject.onAction(CarSelectionAction.UpPressed)
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
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            val stateAfterBuildYearSelection = awaitItem()
            stateAfterBuildYearSelection shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                FuelType.getList()
            )

            // back press on fuel type selection state
            subject.onAction(CarSelectionAction.OnBackPressed)
            val stateAfterBackPressed = awaitItem()
            stateAfterBackPressed shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            // back press on build year selection
            subject.onAction(CarSelectionAction.OnBackPressed)
            val stateAfterBackPressedOnBuildYearSelection = awaitItem()
            stateAfterBackPressedOnBuildYearSelection shouldBe CarSelectionUiState.SeriesSelection(
                brandToSelect,
                series
            )

            // back press on series selection
            subject.onAction(
                CarSelectionAction.OnBackPressed
            )
            val stateAfterBackPressedOnSeriesSelection = awaitItem()
            stateAfterBackPressedOnSeriesSelection shouldBe CarSelectionUiState.BrandSelection(
                brands
            )

            // back press on brand selection
            subject.events.test {
                subject.onAction(
                    CarSelectionAction.OnBackPressed
                )
                awaitItem() shouldBe CarSelectionUiEvent.GoBack
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search icon click should filter the brand list`() = runTest {
        val brands = fakeCars.map { it.brand }.distinct()
        subject.viewState.test {
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brands)

            subject.onAction(CarSelectionAction.SearchAction("bm"))
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(listOf("bmw"))

            subject.onAction(CarSelectionAction.SearchAction("bmww"))
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(emptyList())

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brands)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search icon click should filter the series list`() = runTest {
        val brandToSelect = "audi"

        val brands = fakeCars.map { it.brand }.distinct()
        val series = fakeCars.filter { it.brand == brandToSelect }.map { it.series }

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brands)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandToSelect))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, series)

            subject.onAction(CarSelectionAction.SearchAction("a1"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, listOf("a1"))

            subject.onAction(CarSelectionAction.SearchAction("a11"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, emptyList())

            subject.onAction(CarSelectionAction.SearchAction("a"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(
                brandToSelect,
                listOf("a1", "a2")
            )

            subject.onAction(CarSelectionAction.SearchAction("a2"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, listOf("a2"))

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandToSelect, series)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search action should filter the year list`() = runTest {
        val brandToSelect = "audi"
        val seriesToSelect = "a1"

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
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            subject.onAction(CarSelectionAction.SearchAction("1998"))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                listOf("1998")
            )

            subject.onAction(CarSelectionAction.SearchAction("19999"))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                emptyList()
            )

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            subject.onAction(CarSelectionAction.SearchAction("some text"))

            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandToSelect,
                seriesToSelect,
                emptyList()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search action should filter the fuel type list`() = runTest {
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
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYearToSelect))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                FuelType.getList()
            )

            subject.onAction(CarSelectionAction.SearchAction("Diesel"))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                listOf("Diesel")
            )

            subject.onAction(CarSelectionAction.SearchAction("Diesel1111"))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                emptyList()
            )

            subject.onAction(CarSelectionAction.SearchAction("G"))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                listOf("Gasoline")
            )

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandToSelect,
                seriesToSelect,
                modelYearToSelect,
                FuelType.getList()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
