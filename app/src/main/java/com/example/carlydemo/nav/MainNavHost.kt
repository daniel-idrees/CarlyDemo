package com.example.carlydemo.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.carlydemo.carlist.CarListScreen
import com.example.carlydemo.carselection.CarSelectionScreen
import com.example.carlydemo.dashboard.DashboardScreen

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
