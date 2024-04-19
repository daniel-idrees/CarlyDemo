package com.example.carlydemo.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.carlydemo.ui.theme.FontLight

@Composable
fun TopBar(titleText: String, onBackPress: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spaceS),
    ) {
        Text(
            modifier = Modifier.clickable(onClick = onBackPress),
            text = "<",
            color = FontLight,
            fontSize = 20.sp
        )

        Text(
            text = titleText,
            color = FontLight,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(1f)
        )
    }
}
