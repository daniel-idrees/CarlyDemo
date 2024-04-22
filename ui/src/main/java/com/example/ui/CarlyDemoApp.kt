package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.nav.MainNavHost
import com.example.ui.theme.CarlyDemoTheme

@Composable
fun CarlyDemoApp() {
    CarlyDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent,
        ) {
            MainNavHost()
        }
    }
}
