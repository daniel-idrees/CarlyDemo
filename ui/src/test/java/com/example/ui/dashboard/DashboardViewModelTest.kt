package com.example.ui.dashboard

import app.cash.turbine.test
import com.example.domain.usecase.GetMainSelectedCarUseCase
import com.example.ui.common.MainDispatcherRule
import com.example.ui.utils.FakeObjects.anySelectedCar
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class DashboardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMainSelectedCarUseCase: GetMainSelectedCarUseCase = mock()

    private val subject by lazy {
        DashboardViewModel(
            getMainSelectedCarUseCase,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `view state should be loading initially`() = runTest {
        subject.viewState.value shouldBe DashboardUiState.Loading
    }

    @Test
    fun `view state should be selected car state when a car exist in storage`() = runTest {
        val selectedCar = anySelectedCar

        // when
        whenever(getMainSelectedCarUseCase.get()) doReturn
            flowOf(selectedCar)

        // then
        subject.viewState.test {
            val result = awaitItem()
            result shouldBe DashboardUiState.CarSelectedState(selectedCar)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `view state should be no car selected state when a car does not exist in storage`() =
        runTest {
            // when
            whenever(getMainSelectedCarUseCase.get()) doReturn flowOf(null)

            // then
            subject.viewState.test {
                val result = awaitItem()
                result shouldBe DashboardUiState.NoCarSelectedState
                cancelAndIgnoreRemainingEvents()
            }
        }
}