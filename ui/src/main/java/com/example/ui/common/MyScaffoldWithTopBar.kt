package com.example.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
internal fun MyScaffoldWithTopBar(
    titleText: String,
    onUpPress: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(
                titleText,
                onBackPress = onUpPress
            )
        }
    ) {
        content(it)
    }
}