package com.example.ui.carselection

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.FuelType
import com.example.ui.R
import com.example.ui.common.ErrorView
import com.example.ui.common.LightHorizontalDivider
import com.example.ui.common.Loader
import com.example.ui.common.ProceedIconBox
import com.example.ui.common.TopBar
import com.example.ui.common.spaceS
import com.example.ui.common.spaceXXS
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.FontDark
import com.example.ui.theme.primaryColor

@Composable
internal fun CarSelectionScreen(
    viewModel: CarSelectionViewModel,
    goBack: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    val backPressAction = {
        onBackPressAction(viewState, viewModel, goBack)
    }

    BackHandler {
        backPressAction()
    }

    MainView(
        viewState = viewState,
        onUpPress = backPressAction,
        onBrandSelectAction = viewModel::selectBrand,
        goBack = goBack,
        onSelectSeriesAction = viewModel::selectSeries,
        onSelectModelYearAction = viewModel::selectModelYear,
        onSelectFuelTypeAction = viewModel::selectFuelType
    )
}

@Composable
private fun MainView(
    viewState: CarSelectionUiState,
    onUpPress: () -> Unit,
    onBrandSelectAction: (String) -> Unit,
    goBack: () -> Unit,
    onSelectSeriesAction: (String) -> Unit,
    onSelectModelYearAction: (String) -> Unit,
    onSelectFuelTypeAction: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }

    val onSearchClick = {
        if (text.isNotBlank()) {
            focusManager.clearFocus()
        }
    }

    var searchBarHint by rememberSaveable { mutableStateOf("") }

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
                    textStyle = TextStyle(color = FontDark),
                    label = {
                        Text(
                            text = searchBarHint,
                            color = FontDark
                        )
                    },
                )

                when (viewState) {
                    is CarSelectionUiState.CarSelectionFinished -> goBack() //TODO
                    CarSelectionUiState.Error -> ErrorView()
                    CarSelectionUiState.Loading -> Loader()
                    is CarSelectionUiState.SelectBrand -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_brand_text)
                        BrandSelectionListView(
                            viewState.brands,
                            onItemClick = onBrandSelectAction
                        )
                    }

                    is CarSelectionUiState.SelectSeries -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_brand_text)
                        SeriesSelectionListView(
                            header = viewState.selectedBrand,
                            items = viewState.series,
                            onItemClick = onSelectSeriesAction
                        )
                    }

                    is CarSelectionUiState.SelectYear -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_build_year_text)
                        BuildYearSelectionListView(
                            header = viewState.selectedBrand + ", " + viewState.selectedSeries,
                            minSupportedYear = viewState.minSupportedYear,
                            maxSupportedYear = viewState.maxSupportedYear,
                            onItemClick = onSelectModelYearAction
                        )
                    }

                    is CarSelectionUiState.SelectFuelType -> {
                        searchBarHint =
                            stringResource(id = R.string.car_selection_screen_search_model_text)
                        FuelTypeListView(
                            header = viewState.selectedBrand + ", " + viewState.selectedSeries + ", " + viewState.selectedModelYear,
                            onItemClick = onSelectFuelTypeAction
                        )
                    }
                }
            }
        }
    }
}


private fun onBackPressAction(
    viewState: CarSelectionUiState,
    viewModel: CarSelectionViewModel,
    goBack: () -> Unit
) {
    when (viewState) {
        is CarSelectionUiState.SelectSeries -> viewModel.initialState()
        is CarSelectionUiState.SelectYear -> viewModel.selectBrand(viewState.selectedBrand)
        is CarSelectionUiState.SelectFuelType -> viewModel.selectSeries(viewState.selectedSeries)
        else -> goBack()
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

                        ProceedIconBox()
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
private fun CarBrandSelectionPreview() {
    MainView(
        viewState = CarSelectionUiState.SelectBrand(
            listOf(
                "Bmw",
                "Audi",
                "Mercedes",
                "Volkswagen"
            )
        ),
        onUpPress = { },
        onBrandSelectAction = {},
        goBack = { },
        onSelectSeriesAction = {},
        onSelectModelYearAction = {},
        onSelectFuelTypeAction = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CarSeriesSelectionPreview() {
    MainView(
        viewState = CarSelectionUiState.SelectSeries(
            selectedBrand = "Bmw",
            series = listOf("1 Series", "2 Series", "X1 Series", "X2 Series")
        ),
        onUpPress = { },
        onBrandSelectAction = {},
        goBack = { },
        onSelectSeriesAction = {},
        onSelectModelYearAction = {},
        onSelectFuelTypeAction = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CarYearSelectionPreview() {
    MainView(
        viewState = CarSelectionUiState.SelectYear(
            selectedBrand = "Bmw",
            selectedSeries = "X1 Series",
            minSupportedYear = 2010,
            maxSupportedYear = 2023
        ),
        onUpPress = { },
        onBrandSelectAction = {},
        goBack = { },
        onSelectSeriesAction = {},
        onSelectModelYearAction = {},
        onSelectFuelTypeAction = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CarFuelTypeSelectionPreview() {
    MainView(
        viewState = CarSelectionUiState.SelectFuelType(
            selectedBrand = "Bmw",
            selectedSeries = "X1 Series",
            selectedModelYear = "2023"
        ),
        onUpPress = { },
        onBrandSelectAction = {},
        goBack = { },
        onSelectSeriesAction = {},
        onSelectModelYearAction = {},
        onSelectFuelTypeAction = {}
    )
}
