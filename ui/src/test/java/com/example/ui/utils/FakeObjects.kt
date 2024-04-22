package com.example.ui.utils

import com.example.domain.model.Car
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.model.util.getBrands

internal object FakeObjects {

    val anySelectedCar by lazy {
        SelectedCar(
            id = 99,
            brand = "BMW",
            series = "3 series",
            buildYear = 2018,
            fuelType = FuelType.Diesel,
            features = emptyList(),
            isMain = true
        )
    }

    val fakeSelectedCarList by lazy {
        listOf(
            SelectedCar(
                brand = "BMW",
                series = "3 series",
                buildYear = 2018,
                fuelType = FuelType.Diesel,
                features = emptyList(),
                isMain = true
            ),
            SelectedCar(
                brand = "Audi",
                series = "R",
                buildYear = 2023,
                fuelType = FuelType.Gasoline,
                features = emptyList(),
                isMain = false
            )
        )
    }

    val fakeCars by lazy {
        listOf(
            Car(
                brand = "bmw",
                series = "x1",
                minSupportedYear = 1990,
                maxSupportedYear = 1998,
                features = listOf("Battery Check")
            ),
            Car(
                brand = "audi",
                series = "a1",
                minSupportedYear = 1990,
                maxSupportedYear = 1998,
                features = listOf("Diagnostics")
            ),
            Car(
                brand = "audi",
                series = "a2",
                minSupportedYear = 1990,
                maxSupportedYear = 1998,
                features = listOf("Live Check")
            )
        )
    }

    val brandsFromFakeCars by lazy {
        fakeCars.getBrands()
    }
}
