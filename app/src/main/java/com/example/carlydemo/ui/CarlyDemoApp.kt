package com.example.carlydemo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.carlydemo.ui.nav.MainNavHost
import com.example.carlydemo.ui.theme.CarlyDemoTheme

@Composable
fun CarlyDemoApp() {
    CarlyDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            MainNavHost()
        }
    }
}
