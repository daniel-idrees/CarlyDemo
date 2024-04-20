package com.example.carlydemo.ui.dashboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    private val _viewState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.NoCarSelectedState)

    val viewState: StateFlow<DashboardUiState>
        get() = _viewState

    init {
        //TODO check wether selected car is available
    }
}