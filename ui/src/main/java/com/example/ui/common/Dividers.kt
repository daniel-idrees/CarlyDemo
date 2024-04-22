package com.example.ui.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkGrey
import com.example.ui.theme.LightGrey

@Composable
internal fun DoubleHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = LightGrey,
    )
    HorizontalDivider(
        thickness = 1.dp,
        color = DarkGrey,
    )
}