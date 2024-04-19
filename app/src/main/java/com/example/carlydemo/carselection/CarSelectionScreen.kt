package com.example.carlydemo.carselection

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
import androidx.compose.material3.HorizontalDivider
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
import com.example.carlydemo.R
import com.example.carlydemo.common.LightHorizontalDivider
import com.example.carlydemo.common.ProceedButton
import com.example.carlydemo.common.TopBar
import com.example.carlydemo.common.spaceS
import com.example.carlydemo.common.spaceXXS
import com.example.carlydemo.ui.theme.BackgroundDark
import com.example.carlydemo.ui.theme.BackgroundLight
import com.example.carlydemo.ui.theme.FontDark
import com.example.carlydemo.ui.theme.primaryColor

@Composable
fun CarSelectionScreen(goBack: () -> Unit) {
    val searchBrandText = stringResource(id = R.string.car_selection_screen_search_brand_text)
    val searchBuildYearText =
        stringResource(id = R.string.car_selection_screen_search_build_year_text)
    val searchModelText = stringResource(id = R.string.car_selection_screen_search_model_text)

    //TODO ui state management

    val focusManager = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }

    val onSearchClick = {
        if (text.isNotBlank()) {
            focusManager.clearFocus()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                stringResource(id = R.string.car_selection_screen_top_bar_text),
                onBackPress = goBack
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
                            text = searchBrandText,
                            color = FontDark
                        )
                    },
                )

                BrandSelectionListView(listOf("Audi", "BMW", "Mercedes", "Volkswagen")) {}
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
    onItemClick: () -> Unit
) {
    ListView(items = items, onItemClick = onItemClick)
}

@Composable
private fun SeriesSelectionListView(
    header: String,
    items: List<String>,
    onItemClick: () -> Unit
) {
    ListView(header, items, onItemClick)
}

@Composable
private fun BuildYearSelectionListView(
    header: String,
    items: List<String>,
    onItemClick: () -> Unit
) {
    ListView(header, items, onItemClick)
}


@Composable
private fun FuelTypeListView(
    header: String,
    items: List<String>,
    onItemClick: () -> Unit
) {
    ListView(header, items, onItemClick)
}


@Composable
private fun ListView(
    header: String = "",
    items: List<String>,
    onItemClick: () -> Unit
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
                            .padding(horizontal = spaceS, vertical = spaceXXS),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = text,
                            modifier = Modifier
                                .clickable(onClick = onItemClick),

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
    CarSelectionScreen({})
}