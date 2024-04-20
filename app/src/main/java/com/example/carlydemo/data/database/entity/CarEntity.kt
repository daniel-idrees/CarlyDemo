package com.example.carlydemo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.carlydemo.data.database.util.ListConverter
import com.example.carlydemo.domain.model.FuelType

@Entity(tableName = "car", indices = [Index(value = ["brand_name", "series_name"], unique = true)])
@TypeConverters(ListConverter::class,)
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "brand_name") val brandName: String,
    @ColumnInfo(name = "series_name") val seriesName: String,
    @ColumnInfo(name = "build_year") val buildYear: Int,
    @ColumnInfo(name = "supported_features") val supportedFeatures: List<String>,
    @ColumnInfo(name = "fuel_type") val fuelType: FuelType,
    @ColumnInfo(name = "is_selected_as_main") val isSelectedAsMain: Boolean
)
