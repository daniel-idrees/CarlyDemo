package com.example.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkGrey
import com.example.ui.theme.FontLight

@Composable
internal fun ProceedIconBox() {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(CircleShape)
            .background(DarkGrey),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = ">",
            color = FontLight,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}
