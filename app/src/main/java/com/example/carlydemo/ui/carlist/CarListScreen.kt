package com.example.carlydemo.ui.carlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carlydemo.R
import com.example.carlydemo.ui.common.TopBar
import com.example.carlydemo.ui.common.spaceL
import com.example.carlydemo.ui.common.spaceM
import com.example.carlydemo.ui.common.spaceS
import com.example.carlydemo.ui.common.spaceXS
import com.example.carlydemo.domain.model.Car
import com.example.carlydemo.domain.model.FuelType
import com.example.carlydemo.domain.model.SelectedCar
import com.example.carlydemo.ui.theme.BackgroundDark
import com.example.carlydemo.ui.theme.BackgroundLight
import com.example.carlydemo.ui.theme.FontDark
import com.example.carlydemo.ui.theme.FontLight
import com.example.carlydemo.ui.theme.primaryColor

@Composable
fun CarListScreen(
    navigateToCarSelection: () -> Unit,
    goBack: () -> Unit
) {

    val carList = listOf(
        SelectedCar("BMW", "3 series", 2018, FuelType.Diesel, emptyList()),
        SelectedCar("Audi", "A4", 2007, FuelType.Gasoline, emptyList(), true),
        SelectedCar("Mercedes", "C class", 2008, FuelType.Electric, emptyList()),
    )

    Scaffold(
        topBar = {
            TopBar(stringResource(id = R.string.car_list_screen_top_bar_text), onBackPress = goBack)
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BackgroundLight)
                .padding(contentPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = spaceM),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                CarListView(carList)

                AddNewCarButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = spaceL),
                    onClick = navigateToCarSelection
                )
            }
        }
    }
}

@Composable
private fun CarListView(selectedCars: List<SelectedCar>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(spaceS)
    ) {
        items(selectedCars) { car ->
            CarItemView(car, onDeleteClick = {})
        }
    }
}

@Composable
private fun AddNewCarButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
        onClick = onClick
    ) {
        Text(
            text = stringResource(id = R.string.car_list_screen_add_new_car_button_text),
            color = FontLight
        )
    }
}

@Composable
private fun CarItemView(car: SelectedCar, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceS),
        colors = CardDefaults.cardColors(containerColor = BackgroundDark),
        border = if (car.isMain) BorderStroke(1.dp, color = primaryColor) else null
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
                    color = FontLight,
                    fontSize = 16.sp
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
                color = FontDark,
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
private fun CarListPreview() {
    CarListScreen({}, {})
}
