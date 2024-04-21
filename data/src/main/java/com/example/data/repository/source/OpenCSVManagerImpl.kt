package com.example.data.repository.source

import android.content.Context
import com.example.data.dto.CarDto
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject


private const val dataFileName = "vehicle_data.csv"

private const val BRAND_NAME_INDEX = 0
private const val BRAND_SERIES_INDEX = 1
private const val MIN_SUPPORTED_YEAR_INDEX = 2
private const val MAX_SUPPORTED_YEAR_INDEX = 3
private const val FEATURES_INDEX = 4

internal class OpenCSVManagerImpl @Inject constructor(
    @ApplicationContext
    private val appContext: Context
) : OpenCSVManager {
    @Throws(IOException::class)
    override suspend fun readCarData(): List<CarDto> {
        val inputStream = appContext.assets.open(dataFileName)
        val reader = CSVReaderBuilder(InputStreamReader(inputStream)).build()
        return reader.toCarListData()
    }
}

private fun CSVReader.toCarListData(): List<CarDto> {
    val cars = mutableListOf<CarDto>()

    readNext() // skip the first title line

    var nextLine: Array<String>?
    var lastBrandName = ""
    var lastBrandFeatures = emptyList<String>()

    while (readNext().also { nextLine = it } != null) {
        nextLine?.let { row ->

            if(row[com.example.data.repository.source.BRAND_NAME_INDEX].isNotBlank()) {
                lastBrandName = row[com.example.data.repository.source.BRAND_NAME_INDEX]
            }

            if(row[com.example.data.repository.source.FEATURES_INDEX].isNotBlank()) {
                lastBrandFeatures = row[com.example.data.repository.source.FEATURES_INDEX].split("\n")
            }

            cars.add(
                CarDto(
                    brandName = lastBrandName,
                    seriesName = row[com.example.data.repository.source.BRAND_SERIES_INDEX],
                    minimumSupportedYear = row[com.example.data.repository.source.MIN_SUPPORTED_YEAR_INDEX].toInt(),
                    maximumSupportedYear = row[com.example.data.repository.source.MAX_SUPPORTED_YEAR_INDEX].toInt(),
                    supportedFeatures = lastBrandFeatures,
                )
            )
        }
    }
    return cars
}
