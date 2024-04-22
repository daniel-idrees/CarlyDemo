package com.example.ui

import androidx.compose.runtime.Composable
import com.example.ui.nav.MainNavHost
import com.example.ui.theme.CarlyDemoTheme

@Composable
fun CarlyDemoApp() {
    CarlyDemoTheme {
        MainNavHost()
    }
}
