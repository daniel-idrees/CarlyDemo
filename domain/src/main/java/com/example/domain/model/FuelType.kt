package com.example.domain.model

enum class FuelType {
    Gasoline,
    Diesel,
    Hybrid,
    Electric,
    Other;

    companion object {
        fun getList(): List<String> = entries.map { it.name }
    }
}
