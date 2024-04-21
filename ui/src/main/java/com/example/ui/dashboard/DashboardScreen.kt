package com.example.ui.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.ui.R
import com.example.ui.common.DarkHorizontalDivider
import com.example.ui.common.Loader
import com.example.ui.common.ProceedIconBox
import com.example.ui.common.spaceS
import com.example.ui.common.spaceXS
import com.example.ui.common.spaceXXS
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.FontDark
import com.example.ui.theme.FontLight

@Composable
internal fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigateToCarSelection: () -> Unit,
    navigateToCarList: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    MainView(
        viewState,
        navigateToCarSelection = navigateToCarSelection,
        navigateToCarList = navigateToCarList
    )
}

@Composable
private fun MainView(
    viewState: DashboardUiState,
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
                    .padding(top = spaceXS),
                alignment = Alignment.TopCenter
            )

            when (viewState) {
                is DashboardUiState.Loading -> {
                    Loader()
                }

                is DashboardUiState.CarSelectedState -> {
                    SelectCarDetailView(viewState.car, navigateToCarList)
                }

                else -> {
                    AddButton(
                        modifier = Modifier.weight(1f),
                        navigateToCarSelection
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectCarDetailView(car: SelectedCar, navigateToCarList: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(spaceS)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        id = R.string.car_manufacturer_and_brand_series_text,
                        car.brand,
                        car.series
                    ),
                    color = FontLight,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Image(
                    painter = painterResource(id = R.drawable.switch_car_icon),
                    contentDescription = "Switch car icon",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(onClick = navigateToCarList),
                    alignment = Alignment.TopEnd
                )
            }

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = stringResource(
                    id = R.string.car_manufactured_year_and_fuel_type_text,
                    car.buildYear,
                    car.fuelType
                ),
                color = FontDark,
                fontSize = 14.sp,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.car_image),
            contentDescription = "Car image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spaceXXS)
                .size(150.dp),
            alignment = Alignment.Center
        )

        Column {
            Text(
                text = stringResource(id = R.string.dashboard_feature_list_header_text),
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
                    .padding(vertical = spaceS)
            ) {
                val features = car.features
                LazyColumn {
                    items(features.size) { index ->
                        val feature = features[index]
                        FeatureListItem(feature, onItemClick = {})

                        if (index != features.lastIndex) {
                            DarkHorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureListItem(text: String, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceS, vertical = spaceXXS)
            .clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = FontDark,
            fontSize = 14.sp,
        )
        ProceedIconBox()
    }
}

@Composable
private fun AddButton(
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
private fun DashboardWithNoSelectionPreview() {
    MainView(DashboardUiState.NoCarSelectedState, navigateToCarSelection = { }) {
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardWithCarDetailPreview() {
    MainView(
        DashboardUiState.CarSelectedState(
            SelectedCar(
                null,
                "BMW",
                "3 series",
                2018,
                FuelType.Diesel,
                listOf("Diagnostics", "Live Data", "Battery Check", "Car Check")
            )
        ),
        navigateToCarSelection = { }) {
    }
}
