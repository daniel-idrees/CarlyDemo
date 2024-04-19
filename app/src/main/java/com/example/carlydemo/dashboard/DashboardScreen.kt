package com.example.carlydemo.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carlydemo.R
import com.example.carlydemo.ui.theme.BackgroundDark
import com.example.carlydemo.ui.theme.BackgroundLight
import com.example.carlydemo.ui.theme.CarlyDemoTheme
import com.example.carlydemo.ui.theme.FontDark
import com.example.carlydemo.ui.theme.FontLight

@Composable
fun DashboardScreen(
    navigateToCarSelection: () -> Unit,
    navigateToCarList: () -> Unit
) {
    Box(modifier = Modifier.background(color = BackgroundDark)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.dashboard_background),
                    contentScale = ContentScale.FillBounds,
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.company_logo),
                contentDescription = "",
                modifier = Modifier
                    .aspectRatio(20f / 6f)
                    .padding(top = 10.dp),
                alignment = Alignment.TopCenter
            )

            val isCarSelected = false

            if (isCarSelected) {
                WithCarSelectedView(navigateToCarList)
            } else {
                NoCarSelectedView(
                    modifier = Modifier.weight(1f),
                    navigateToCarSelection
                )
            }
        }
    }
}

@Composable
private fun WithCarSelectedView(navigateToCarList: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "AUDI - A4",
                color = FontLight,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(id = R.drawable.switch_car_icon),
                contentDescription = "",
                modifier = Modifier
                    .weight(1f)
                    .size(30.dp)
                    .clickable(onClick = navigateToCarList),
                alignment = Alignment.TopEnd
            )
        }

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = "2007 - Gasoline",
            color = FontDark,
            fontSize = 14.sp,
        )
        Image(
            painter = painterResource(id = R.drawable.car_image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .weight(1f),
            alignment = Alignment.Center
        )

        Text(
            text = "Discover your car",
            color = FontLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundLight,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Diagnostics",
                        color = FontDark,
                        fontSize = 14.sp,
                    )

                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(BackgroundDark),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = ">",
                            color = FontLight,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = BackgroundDark
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Live Data",
                        color = FontDark,
                        fontSize = 14.sp,
                    )

                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(BackgroundDark),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = ">",
                            color = FontLight,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = BackgroundDark
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Battery Check",
                        color = FontDark,
                        fontSize = 14.sp,
                    )

                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(BackgroundDark),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = ">",
                            color = FontLight,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoCarSelectedView(
    modifier: Modifier,
    navigateToCarSelection: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.add_car_icon),
        contentDescription = "",
        alpha = 0.7f,
        modifier = modifier
            .aspectRatio(20f / 5f)
            .clickable(onClick = navigateToCarSelection)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardPreview() {
    CarlyDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            DashboardScreen({}, {})
        }
    }
}
