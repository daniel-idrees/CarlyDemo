package com.example.carlydemo.data.dto.mapper

import com.example.carlydemo.data.dto.CarDto
import com.example.carlydemo.domain.model.Car

internal fun List<CarDto>.toCarList(): List<Car> {
    val cars = mutableListOf<Car>()
    forEach { dto ->
        cars.add(
            Car(
                dto.brandName,
                dto.seriesName,
                dto.minimumSupportedYear,
                dto.maximumSupportedYear,
                dto.supportedFeatures
            )
        )
    }
    return cars
}
