package com.example.carlydemo.domain.model

import com.example.carlydemo.data.database.entity.CarEntity

data class SelectedCar(
    val id: Long? = null,
    val brand: String,
    val series: String,
    val buildYear: Int,
    val fuelType: FuelType,
    val features: List<String>,
    val isMain: Boolean = false
)
