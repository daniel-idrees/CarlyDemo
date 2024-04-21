package com.example.data.dto

internal data class CarDto(
    val brandName: String,
    val seriesName: String,
    val minimumSupportedYear: Int,
    val maximumSupportedYear: Int,
    val supportedFeatures: List<String>
)
