package com.example.ui.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BackgroundLight

@Composable
internal fun DoubleHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = BackgroundLight,
    )
    HorizontalDivider(
        thickness = 1.dp,
        color = BackgroundDark,
    )
}