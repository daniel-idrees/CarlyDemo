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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.common.DoubleHorizontalDivider
import com.example.ui.common.Loader
import com.example.ui.common.ProceedIconBox
import com.example.ui.common.spaceS
import com.example.ui.common.spaceXS
import com.example.ui.common.spaceXXS
import com.example.ui.theme.DarkGrey
import com.example.ui.theme.LightGrey
import com.example.ui.theme.CarlyDemoTheme
import com.example.ui.theme.FontLight
import com.example.ui.theme.MyTypography

@Composable
internal fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigateToCarSelection: () -> Unit,
    navigateToCarList: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                DashboardUiEvent.NavigateToCarList -> {
                    navigateToCarList()
                }

                DashboardUiEvent.NavigateToCarSelection -> {
                    navigateToCarSelection()
                }

                DashboardUiEvent.NavigateToFeatureScreen -> {
                    // no-op
                }
            }
        }
    }

    MainView(
        viewState,
        viewModel::onAction,
    )
}

@Composable
private fun MainView(
    viewState: DashboardUiState,
    onAction: (DashboardAction) -> Unit,
) {
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
                .padding(top = spaceXXS),
            alignment = Alignment.TopCenter
        )

        when (viewState) {
            is DashboardUiState.Loading -> {
                Loader()
            }

            is DashboardUiState.CarSelectedState -> {
                CarDetailView(viewState.car, onAction)
            }

            else -> {
                AddButton(
                    modifier = Modifier.weight(1f),
                    onButtonClick = { onAction(DashboardAction.AddButtonClicked) }
                )
            }
        }
    }

}

@Composable
private fun CarDetailView(
    car: SelectedCar,
    onAction: (DashboardAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(spaceS)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
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
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    style = MyTypography.titleMedium
                )

                Image(
                    painter = painterResource(id = R.drawable.switch_car_icon),
                    contentDescription = "Switch car icon",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onAction(DashboardAction.SwitchCarIconClicked)
                        },
                    alignment = Alignment.TopEnd
                )
            }

            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = stringResource(
                    id = R.string.car_manufactured_year_and_fuel_type_text,
                    car.buildYear,
                    car.fuelType
                ),
                style = MyTypography.bodyMedium
            )
        }
        Image(
            painter = painterResource(id = R.drawable.car_image),
            contentDescription = "Car image",
            modifier = Modifier
                .aspectRatio(20f / 10f),
            alignment = Alignment.Center
        )

        Column {
            Text(
                text = stringResource(id = R.string.dashboard_feature_list_header_text),
                color = FontLight,
                fontSize = 20.sp,
                style = MyTypography.bodyMedium
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spaceXS)

            ) {
                Box(
                    modifier = Modifier.background(
                        brush = Brush.linearGradient(
                            listOf(
                                DarkGrey,
                                LightGrey,
                            )
                        )
                    )
                ) {
                    val features = car.features
                    LazyColumn {
                        items(features.size) { index ->
                            val feature = features[index]
                            FeatureListItem(feature, onItemClick = {
                                onAction(DashboardAction.FeatureItemClicked)
                            })

                            if (index != features.lastIndex) {
                                DoubleHorizontalDivider()
                            }
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
            .padding(horizontal = spaceS, vertical = spaceXS)
            .clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MyTypography.bodyMedium
        )
        ProceedIconBox()
    }
}

@Composable
private fun AddButton(
    modifier: Modifier,
    onButtonClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.add_car_icon),
        contentDescription = "",
        alpha = 0.7f,
        modifier = modifier
            .aspectRatio(20f / 5f)
            .clickable(onClick = onButtonClick)
    )
}

@Composable
private fun DashboardUiState.Preview() {
    CarlyDemoTheme {
        MainView(this) {}
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardWithNoSelectionPreview() {
    DashboardUiState.NoCarSelectedState.Preview()
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardWithCarDetailPreview() {
    DashboardUiState.CarSelectedState(
        SelectedCar(
            null,
            "BMW",
            "3 series",
            2018,
            FuelType.Diesel,
            listOf("Diagnostics", "Live Data", "Battery Check", "Car Check")
        )
    ).Preview()
}
