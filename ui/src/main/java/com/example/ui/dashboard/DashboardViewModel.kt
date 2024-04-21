package com.example.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DefaultDispatcher
import com.example.domain.usecase.GetMainSelectedCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getMainSelectedCarUseCase: GetMainSelectedCarUseCase,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val actions: MutableSharedFlow<DashboardAction> =
        MutableSharedFlow(extraBufferCapacity = 64)

    private val _events = Channel<DashboardUiEvent>(capacity = 32)
    val events: Flow<DashboardUiEvent> = _events.receiveAsFlow()

    val viewState: StateFlow<DashboardUiState> =
        getMainSelectedCarUseCase.get()
            .catch { DashboardUiState.NoCarSelectedState }
            .distinctUntilChanged()
            .filterNotNull()
            .mapLatest { car ->
                DashboardUiState.CarSelectedState(car)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = DashboardUiState.NoCarSelectedState
            )

    init {
        actions
            .onEach(::handleAction)
            .flowOn(defaultDispatcher)
            .launchIn(viewModelScope)
    }

    fun onAction(action: DashboardAction) = actions.tryEmit(action)

    private suspend fun handleAction(action: DashboardAction) {
        when (action) {
            DashboardAction.AddButtonClicked -> _events.send(DashboardUiEvent.NavigateToCarSelection)
            DashboardAction.FeatureItemClicked -> _events.send(DashboardUiEvent.NavigateToFeatureScreen)
            DashboardAction.SwitchCarIconClicked -> _events.send(DashboardUiEvent.NavigateToCarList)
        }
    }
}
