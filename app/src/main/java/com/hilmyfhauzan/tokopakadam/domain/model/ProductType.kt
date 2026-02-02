package com.hilmyfhauzan.tokopakadam.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ProductType(
        val displayName: String,
        val pricePerPiece: Long, // Harga per butir/biji
        val isEgg: Boolean // Helper untuk membedakan Telur & Tahu
) {
    EGG_SMALL("Telur Kecil", 1800L, true),
    EGG_MEDIUM("Telur Sedang", 1900L, true),
    EGG_LARGE("Telur Besar", 2000L, true),
    TOFU("Tahu", 10000L, false);

    // Helper untuk UI Label
    fun unitName(): String {
        return if (isEgg) "Butir" else "Bungkus"
    }

    fun defaultUnit(): SalesUnit {
        return if (isEgg) SalesUnit.GRAIN else SalesUnit.PIECE
    }
}
