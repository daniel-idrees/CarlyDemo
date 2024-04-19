package com.example.carlydemo.data.dto

data class CarDto(
    val brandName: String,
    val seriesName: String,
    val minimumSupportedYear: Int,
    val maximumSupportedYear: Int,
    val supportedFeatures: List<String>
)
