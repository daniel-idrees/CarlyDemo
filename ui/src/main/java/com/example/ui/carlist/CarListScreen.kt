package com.example.ui.carlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.ui.R
import com.example.ui.common.ErrorView
import com.example.ui.common.Loader
import com.example.ui.common.MyScaffoldWithTopBar
import com.example.ui.common.spaceL
import com.example.ui.common.spaceS
import com.example.ui.common.spaceXS
import com.example.ui.theme.DarkGrey
import com.example.ui.theme.CarlyDemoTheme
import com.example.ui.theme.FontLight
import com.example.ui.theme.MyTypography
import com.example.ui.theme.OrangeColor
import com.example.ui.theme.YellowColor

@Composable
internal fun CarListScreen(
    viewModel: CarListViewModel,
    navigateToCarSelection: () -> Unit,
    goBack: () -> Unit
) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                CarListUiEvent.NavigateToCarSelection -> navigateToCarSelection()
                CarListUiEvent.goBack -> goBack()
            }
        }
    }

    when (viewState) {
        is CarListUiState.SelectedCars -> MainView(
            viewState = viewState as CarListUiState.SelectedCars,
            navigateToCarSelection = navigateToCarSelection,
            onAction = viewModel::onAction,
        )

        CarListUiState.Error -> ErrorView()
        CarListUiState.Loading -> Loader()
    }
}

@Composable
private fun MainView(
    viewState: CarListUiState.SelectedCars,
    navigateToCarSelection: () -> Unit,
    onAction: (CarListAction) -> Unit,
) {
    MyScaffoldWithTopBar(
        stringResource(id = R.string.car_list_screen_top_bar_text),
        onUpPress = { onAction(CarListAction.UpPressed) }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            CarListView(
                modifier = Modifier.weight(1f),
                viewState.selectedCars,
                onAction
            )
            Row(
                modifier = Modifier
                    .weight(0.12f)
                    .padding(bottom = spaceXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AddNewCarButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = spaceL, end = spaceL),
                    onClick = navigateToCarSelection
                )
            }
        }
    }
}

@Composable
private fun CarListView(
    modifier: Modifier,
    selectedCars: List<SelectedCar>,
    onAction: (CarListAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spaceS)
    ) {
        items(selectedCars) { car ->
            CarItemView(
                car,
                onCardClick = { onAction(CarListAction.CarItemClicked(car)) },
                onDeleteClick = { onAction(CarListAction.DeleteIconClicked(car)) })
        }
    }
}

@Composable
private fun AddNewCarButton(modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        OrangeColor,
                        YellowColor
                    )
                )
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = onClick
        ) {
            Text(
                text = stringResource(id = R.string.car_list_screen_add_new_car_button_text),
                color = FontLight
            )
        }
    }
}

@Composable
private fun CarItemView(
    car: SelectedCar,
    onCardClick: (Long?) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceS)
            .clickable {
                onCardClick(car.id)
            },
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        border = if (car.isMain) BorderStroke(1.dp, color = OrangeColor) else null
    ) {
        Column(
            modifier = Modifier.padding(vertical = spaceXS, horizontal = spaceS),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        id = R.string.car_manufacturer_and_brand_series_text,
                        car.brand,
                        car.series
                    ),
                    style = MyTypography.titleMedium
                )
                if (!car.isMain) {
                    Image(
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onDeleteClick),
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = "Delete icon"
                    )
                }
            }

            Text(
                text = stringResource(
                    id = R.string.car_manufactured_year_and_fuel_type_text,
                    car.buildYear,
                    car.fuelType
                ),
                style = MyTypography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
private fun CarListPreview() {
    CarlyDemoTheme {
        MainView(
            CarListUiState.SelectedCars(
                listOf(
                    SelectedCar(
                        brand = "BMW",
                        series = "3 series",
                        buildYear = 2018,
                        fuelType = FuelType.Diesel,
                        features = emptyList()
                    ),
                    SelectedCar(
                        brand = "Audi",
                        series = "A4",
                        buildYear = 2007,
                        fuelType = FuelType.Gasoline,
                        features = emptyList(),
                        isMain = true
                    ),
                    SelectedCar(
                        brand = "Mercedes",
                        series = "C class",
                        buildYear = 2008,
                        fuelType = FuelType.Electric,
                        features = emptyList()
                    ),
                )
            ), {}, {})
    }
}
