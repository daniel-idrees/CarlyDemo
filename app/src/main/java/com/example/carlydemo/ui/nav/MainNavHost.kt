package com.example.carlydemo.ui.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
            CarSelectionScreen(
                viewModel = hiltViewModel(),
                goBack = navController::navigateUp
            )
        }

        carListScreen {
            CarListScreen(
                navigateToCarSelection = navController::navigateToCarSelection,
                goBack = navController::navigateUp
            )
        }
    }
}
