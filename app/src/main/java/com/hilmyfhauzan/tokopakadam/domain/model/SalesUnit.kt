package com.hilmyfhauzan.tokopakadam.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SalesUnit(val label: String) {
    GRAIN("Butir"), // Untuk Telur
    PIECE("Biji")   // Untuk Tahu
}