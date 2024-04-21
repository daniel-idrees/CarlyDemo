package com.example.ui.dashboard


sealed interface DashboardAction {
    data object SwitchCarIconClicked : DashboardAction
    data object FeatureItemClicked : DashboardAction
    data object AddButtonClicked : DashboardAction
}
