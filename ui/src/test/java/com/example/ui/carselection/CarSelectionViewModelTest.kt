package com.example.ui.carselection

import app.cash.turbine.test
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.AddSelectedCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import com.example.ui.common.MainDispatcherRule
import com.example.ui.utils.FakeObjects.brandsFromFakeCars
import com.example.ui.utils.FakeObjects.fakeCars
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
        val brandAudi = "audi"
        val seriesA1 = "a1"
        val modelYear1998 = "1998"
        val fuelTypeDiesel = "Diesel"

        val seriesForAudiBrand = listOf("a1", "a2")
        val buildYearListForA1Audi =
            listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandAudi))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesA1))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYear1998))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                FuelType.getList()
            )

            //after fuel type selection
            subject.onAction(CarSelectionAction.OnFuelTypeSelected(fuelTypeDiesel))

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
    fun `vie state should be updated on up press action`() = runTest {
        val brandAudi = "audi"
        val seriesA1 = "a1"
        val modelYear1998 = "1998"

        val seriesForAudiBrand = listOf("a1", "a2")
        val buildYearListForA1Audi =
            listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")

        subject.viewState.test {

            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandAudi))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesA1))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYear1998))
            val stateAfterBuildYearSelection = awaitItem()
            stateAfterBuildYearSelection shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                FuelType.getList()
            )

            // up press on fuel type selection state
            subject.onAction(CarSelectionAction.UpPressed)
            val stateAfterBackPressed = awaitItem()
            stateAfterBackPressed shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            // up press on build year selection
            subject.onAction(CarSelectionAction.UpPressed)
            val stateAfterBackPressedOnBuildYearSelection = awaitItem()
            stateAfterBackPressedOnBuildYearSelection shouldBe CarSelectionUiState.SeriesSelection(
                brandAudi,
                seriesForAudiBrand
            )

            // up press on series selection
            subject.onAction(CarSelectionAction.UpPressed)
            val stateAfterBackPressedOnSeriesSelection = awaitItem()
            stateAfterBackPressedOnSeriesSelection shouldBe CarSelectionUiState.BrandSelection(
                brandsFromFakeCars
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
        val brandAudi = "audi"
        val seriesA1 = "a1"
        val modelYear1998 = "1998"

        val seriesForAudiBrand = listOf("a1", "a2")
        val buildYearListForA1Audi =
            listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandAudi))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesA1))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYear1998))
            val stateAfterBuildYearSelection = awaitItem()
            stateAfterBuildYearSelection shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                FuelType.getList()
            )

            // back press on fuel type selection state
            subject.onAction(CarSelectionAction.OnBackPressed)
            val stateAfterBackPressed = awaitItem()
            stateAfterBackPressed shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            // back press on build year selection
            subject.onAction(CarSelectionAction.OnBackPressed)
            val stateAfterBackPressedOnBuildYearSelection = awaitItem()
            stateAfterBackPressedOnBuildYearSelection shouldBe CarSelectionUiState.SeriesSelection(
                brandAudi,
                seriesForAudiBrand
            )

            // back press on series selection
            subject.onAction(
                CarSelectionAction.OnBackPressed
            )
            val stateAfterBackPressedOnSeriesSelection = awaitItem()
            stateAfterBackPressedOnSeriesSelection shouldBe CarSelectionUiState.BrandSelection(
                brandsFromFakeCars
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
        subject.viewState.test {
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            subject.onAction(CarSelectionAction.SearchAction("bm"))
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(listOf("bmw"))

            subject.onAction(CarSelectionAction.SearchAction("bmww"))
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(emptyList())

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search icon click should filter the series list`() = runTest {
        val brandAudi = "audi"

        val seriesForAudiBrand = listOf("a1", "a2")

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandAudi))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            subject.onAction(CarSelectionAction.SearchAction("a1"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, listOf("a1"))

            subject.onAction(CarSelectionAction.SearchAction("a11"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, emptyList())

            subject.onAction(CarSelectionAction.SearchAction("a"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(
                brandAudi,
                listOf("a1", "a2")
            )

            subject.onAction(CarSelectionAction.SearchAction("a2"))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, listOf("a2"))

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search action should filter the year list`() = runTest {
        val brandAudi = "audi"
        val seriesA1 = "a1"

        val seriesForAudiBrand = listOf("a1", "a2")
        val buildYearListForA1Audi =
            listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandAudi))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesA1))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            subject.onAction(CarSelectionAction.SearchAction("1998"))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                listOf("1998")
            )

            subject.onAction(CarSelectionAction.SearchAction("19999"))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                emptyList()
            )

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                buildYearListForA1Audi
            )

            subject.onAction(CarSelectionAction.SearchAction("some text"))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                emptyList()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search action should filter the fuel type list`() = runTest {
        val brandAudi = "audi"
        val seriesA1 = "a1"
        val modelYear1998 = "1998"

        val seriesForAudiBrand = listOf("a1", "a2")

        subject.viewState.test {
            // initial brand select state
            awaitItem() shouldBe CarSelectionUiState.BrandSelection(brandsFromFakeCars)

            // after brand selection
            subject.onAction(CarSelectionAction.OnBrandSelected(brandAudi))
            awaitItem() shouldBe CarSelectionUiState.SeriesSelection(brandAudi, seriesForAudiBrand)

            // after series selection
            subject.onAction(CarSelectionAction.OnSeriesSelected(seriesA1))
            awaitItem() shouldBe CarSelectionUiState.BuildYearSelection(
                brandAudi,
                seriesA1,
                listOf("1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998")
            )

            //after build year selection
            subject.onAction(CarSelectionAction.OnBuildYearSelected(modelYear1998))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                FuelType.getList()
            )

            subject.onAction(CarSelectionAction.SearchAction("Diesel"))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                listOf("Diesel")
            )

            subject.onAction(CarSelectionAction.SearchAction("Diesel1111"))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                emptyList()
            )

            subject.onAction(CarSelectionAction.SearchAction("G"))
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                listOf("Gasoline")
            )

            subject.onAction(CarSelectionAction.SearchTextEmpty)
            awaitItem() shouldBe CarSelectionUiState.FuelTypeSelection(
                brandAudi,
                seriesA1,
                modelYear1998,
                FuelType.getList()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
