package com.example.data.dto.mapper

import com.example.data.dto.CarDto
import com.example.domain.model.Car


internal fun CarDto.toCar(): Car =
    Car(
        brandName,
        seriesName,
        minimumSupportedYear,
        maximumSupportedYear,
        supportedFeatures
    )
