package com.example.carlydemo.domain.model

data class Car(
    val brand: String,
    val series: String,
    val minSupportedYear: Int,
    val maxSupportedYear: Int,
    val features: List<String>,
)
