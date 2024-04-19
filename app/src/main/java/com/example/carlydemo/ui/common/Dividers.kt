package com.example.carlydemo.ui.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.carlydemo.ui.theme.BackgroundDark
import com.example.carlydemo.ui.theme.BackgroundLight

@Composable
fun LightHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = BackgroundLight
    )
}

@Composable
fun DarkHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = BackgroundDark
    )
}