package com.example.ui.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.ui.carlist.CarListScreen
import com.example.ui.carselection.CarSelectionScreen
import com.example.ui.dashboard.DashboardScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DashboardScreen.route,
    ) {
        dashboardScreen {
            DashboardScreen(
                viewModel = hiltViewModel(),
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
                viewModel = hiltViewModel(),
                navigateToCarSelection = navController::navigateToCarSelection,
                goBack = navController::navigateUp
            )
        }
    }
}
