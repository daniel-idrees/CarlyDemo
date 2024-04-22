package com.example.domain.model.util

import com.example.domain.model.Car

fun List<Car>.getBrands(): List<String> = map { it.brand }.distinct()

fun List<Car>.getSeriesForBrand(brand: String): List<String> =
    filter { it.brand == brand }.map { it.series }

fun List<Car>.getCarForBrandAndSeries(brand: String, series: String): Car =
    first { it.brand == brand && it.series == series }