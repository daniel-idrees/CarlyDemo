package com.example.carlydemo.domain.model

data class Car(
    val manufacturer: String,
    val brandSeries: String,
    val manufacturedYear: String,
    val fuelType: String,
    val selected: Boolean = false
)
