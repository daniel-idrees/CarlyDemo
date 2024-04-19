package com.example.carlydemo.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.carlydemo.ui.carlist.CarListScreen
import com.example.carlydemo.ui.carselection.CarSelectionScreen
import com.example.carlydemo.ui.dashboard.DashboardScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DashboardScreen.route,
    ) {
        dashboardScreen {
            DashboardScreen(
                navigateToCarSelection = navController::navigateToCarSelection,
                navigateToCarList = navController::navigateToCarList
            )
        }

        carSelectionScreen {
            CarSelectionScreen(goBack = navController::popBackStack)
        }

        carListScreen {
            CarListScreen(
                navigateToCarSelection = navController::navigateToCarSelection,
                goBack = navController::popBackStack
            )
        }
    }
}
