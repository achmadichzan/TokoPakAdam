package com.hilmyfhauzan.tokopakadam.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionItem(
    val productType: ProductType,
    val quantity: Double, // Double agar bisa menampung 0.5 (setengah rak)
    val unit: SalesUnit,
    val pricePerUnit: Long, // Harga saat transaksi terjadi (penting jika harga berubah besok)
    val subTotal: Long // quantity * pricePerUnit
)