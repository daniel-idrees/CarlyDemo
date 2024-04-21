package com.example.domain.model.mapper

import com.example.data.database.entity.CarEntity
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar

internal fun CarEntity.toSelectedCar(): SelectedCar =
    SelectedCar(
        id,
        brandName,
        seriesName,
        buildYear,
        FuelType.valueOf(fuelType),
        supportedFeatures,
        isSelectedAsMain
    )

internal fun SelectedCar.asEntity() =
    CarEntity(
        brandName = brand,
        seriesName = series,
        buildYear = buildYear,
        supportedFeatures = features,
        fuelType = fuelType.name,
        isSelectedAsMain = isMain
    )