package com.example.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.ui.nav.Screen.CarListScreen
import com.example.ui.nav.Screen.CarSelectionScreen
import com.example.ui.nav.Screen.DashboardScreen

internal const val DASHBOARD_ROUTE = "dashboard"
internal const val CAR_LIST_ROUTE = "carList"
internal const val CAR_SELECTION_ROUTE = "carSelection"

internal fun NavGraphBuilder.dashboardScreen(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(DashboardScreen.route, content = content)


internal fun NavController.navigateToDashboard() {
    this.navigate(
        DashboardScreen.route,
        navOptions {
            this.launchSingleTop = true
        },
    )
}

internal fun NavGraphBuilder.carListScreen(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(CarListScreen.route, content = content)

internal fun NavController.navigateToCarList() {
    this.navigate(
        CarListScreen.route,
        navOptions {
            this.launchSingleTop = true
        },
    )
}

internal fun NavGraphBuilder.carSelectionScreen(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(CarSelectionScreen.route, content = content)

internal fun NavController.navigateToCarSelection() {
    this.navigate(
        CarSelectionScreen.route,
        navOptions {
            this.launchSingleTop = true
        },
    )
}
