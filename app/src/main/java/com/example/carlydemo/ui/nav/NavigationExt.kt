package com.example.carlydemo.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions

const val DASHBOARD_ROUTE = "dashboard"
const val CAR_LIST_ROUTE = "carList"
const val CAR_SELECTION_ROUTE = "carSelection"

fun NavGraphBuilder.dashboardScreen(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(DashboardScreen.route, content = content)


fun NavController.navigateToDashboard() {
    this.navigate(
        DashboardScreen.route,
        navOptions {
            this.launchSingleTop = true
        },
    )
}

fun NavGraphBuilder.carListScreen(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(CarListScreen.route, content = content)

fun NavController.navigateToCarList() {
    this.navigate(
        CarListScreen.route,
        navOptions {
            this.launchSingleTop = true
        },
    )
}

fun NavGraphBuilder.carSelectionScreen(
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(CarSelectionScreen.route, content = content)

fun NavController.navigateToCarSelection() {
    this.navigate(
        CarSelectionScreen.route,
        navOptions {
            this.launchSingleTop = true
        },
    )
}
