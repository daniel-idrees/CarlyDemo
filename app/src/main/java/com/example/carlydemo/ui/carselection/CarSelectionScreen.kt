package com.example.carlydemo.ui.carselection

import android.content.res.Configuration
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.carlydemo.R
import com.example.carlydemo.domain.model.FuelType
import com.example.carlydemo.ui.common.LightHorizontalDivider
import com.example.carlydemo.ui.common.ProceedButton
import com.example.carlydemo.ui.common.TopBar
import com.example.carlydemo.ui.common.spaceS
import com.example.carlydemo.ui.common.spaceXXS
import com.example.carlydemo.ui.theme.BackgroundDark
import com.example.carlydemo.ui.theme.FontDark
import com.example.carlydemo.ui.theme.primaryColor

@Composable
fun CarSelectionScreen(
    viewModel: CarSelectionViewModel,
    goBack: () -> Unit
) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }

    var searchBarHint by rememberSaveable { mutableStateOf("") }

    val onSearchClick = {
        if (text.isNotBlank()) {
            focusManager.clearFocus()
        }
    }

    val onUpPress = {
        when(viewState) {
            is CarSelectionUiState.SelectSeries -> viewModel.initialState()
            is CarSelectionUiState.SelectYear -> viewModel.selectBrand((viewState as CarSelectionUiState.SelectYear).selectedBrand)
            is CarSelectionUiState.SelectFuelType -> viewModel.selectSeries((viewState as CarSelectionUiState.SelectFuelType).selectedSeries)
            else -> goBack()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                stringResource(id = R.string.car_selection_screen_top_bar_text),
                onBackPress = onUpPress
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .background(color = BackgroundDark)
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = spaceS, end = spaceS),
                    trailingIcon = { SearchButton(onSearchClick) },
                    value = text,
                    singleLine = true,
                    onValueChange = { text = it },
                    label = {
                        Text(
                            text = searchBarHint,
                            color = FontDark
                        )
                    },
                )

                when (viewState) {
                    is CarSelectionUiState.CarSelectionFinished -> goBack() //TODO
                    CarSelectionUiState.Error -> {} //TODO
                    CarSelectionUiState.Loading -> {} // TODO
                    is CarSelectionUiState.SelectBrand -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_brand_text)
                        BrandSelectionListView(
                            (viewState as CarSelectionUiState.SelectBrand).brands,
                            onItemClick = viewModel::selectBrand
                        )
                    }

                    is CarSelectionUiState.SelectSeries -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_brand_text)
                        SeriesSelectionListView(
                            header = (viewState as CarSelectionUiState.SelectSeries).selectedBrand,
                            items = (viewState as CarSelectionUiState.SelectSeries).series,
                            onItemClick = viewModel::selectSeries
                        )
                    }

                    is CarSelectionUiState.SelectYear -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_build_year_text)
                        BuildYearSelectionListView(
                            header = (viewState as CarSelectionUiState.SelectYear).selectedBrand + ", " + (viewState as CarSelectionUiState.SelectYear).selectedSeries,
                            minSupportedYear = (viewState as CarSelectionUiState.SelectYear).minSupportedYear,
                            maxSupportedYear = (viewState as CarSelectionUiState.SelectYear).maxSupportedYear,
                            onItemClick = viewModel::selectModelYear
                        )
                    }

                    is CarSelectionUiState.SelectFuelType -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_model_text)
                        FuelTypeListView(
                            header = (viewState as CarSelectionUiState.SelectFuelType).selectedBrand + ", " + (viewState as CarSelectionUiState.SelectFuelType).selectedSeries + ", " + (viewState as CarSelectionUiState.SelectFuelType).selectedModelYear,
                            onItemClick = viewModel::selectFuelType
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchButton(onSearchClick: () -> Unit) {
    Image(
        modifier = Modifier
            .clickable(onClick = onSearchClick)
            .size(24.dp),
        painter = painterResource(
            id = R.drawable.search_icon,
        ),
        contentDescription = "Search icon",
    )
}

@Composable
private fun BrandSelectionListView(
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    ListView(items = items, onItemClick = onItemClick)
}

@Composable
private fun SeriesSelectionListView(
    header: String,
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    ListView(header, items, onItemClick)
}

@Composable
private fun BuildYearSelectionListView(
    header: String,
    minSupportedYear: Int,
    maxSupportedYear: Int,
    onItemClick: (String) -> Unit
) {
    val yearsList = (minSupportedYear..maxSupportedYear).map { it.toString() }
    ListView(header, yearsList, onItemClick)
}


@Composable
private fun FuelTypeListView(
    header: String,
    onItemClick: (String) -> Unit
) {
    ListView(header, FuelType.getList(), onItemClick)
}

@Composable
private fun ListView(
    header: String = "",
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = spaceS)
    ) {
        Text(
            text = header,
            modifier = Modifier.padding(horizontal = spaceS, vertical = spaceXXS),
            color = primaryColor,
        )

        LightHorizontalDivider()

        LazyColumn {
            items(items) { text ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spaceS, vertical = spaceXXS)
                            .clickable {
                                onItemClick(text)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = text,
                            color = FontDark
                        )

                        ProceedButton {}
                    }

                    LightHorizontalDivider()
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CarSelectionPreview() {
    //CarSelectionScreen({)
}
