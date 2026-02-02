package com.hilmyfhauzan.tokopakadam.presentation.state

import com.hilmyfhauzan.tokopakadam.domain.model.ProductType

data class MainUiState(
    val selectedProduct: ProductType = ProductType.EGG_SMALL,

    // Quantity murni jumlah butir (misal: 30.0)
    val quantities: Map<ProductType, Double> = emptyMap(),
    val cashInput: Long = 0L,
    val isLoading: Boolean = false,
    val isTransactionSaved: Boolean = false,
    val errorMessage: String? = null,
    val activeInput: ActiveInput = ActiveInput.QUANTITY,
    val isLastInputFromNumpad: Boolean = false
) {
    // Computed Properties (Hitungan Otomatis untuk UI)

    val totalBelanja: Long
        get() =
            quantities.entries.sumOf { (type, qty) ->
                // Rumus simpel: Jumlah Butir * Harga Per Butir
                (qty * type.pricePerPiece).toLong()
            }

    val kembali: Long
        get() = if (cashInput > totalBelanja) cashInput - totalBelanja else 0L

    val hutang: Long
        get() = if (cashInput < totalBelanja) totalBelanja - cashInput else 0L

    // Helper untuk menampilkan input terkini di layar (misal: "2.5")
    fun getCurrentQtyDisplay(): String {
        val qty = quantities[selectedProduct] ?: 0.0
        return if (qty == 0.0) "0" else qty.toInt().toString()
    }
}

enum class ActiveInput {
    QUANTITY,
    CASH
}
