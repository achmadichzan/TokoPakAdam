package com.hilmyfhauzan.tokopakadam.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Long = 0, // 0 menandakan data baru (belum masuk DB)
    val timestamp: Long, // Waktu transaksi (Epoch Millis)
    val customerName: String?, // Nullable. Jika null = "Pelanggan Umum"
    val items: List<TransactionItem>, // Daftar belanjaan
    val totalPrice: Long, // Total Belanja
    val amountPaid: Long, // Tunai yang dibayarkan
    val note: String? = null // Catatan opsional
) {
    // Logika Bisnis: Menghitung sisa hutang
    // Jika bayar kurang dari total, maka sisa adalah hutang.
    val remainingDebt: Long
        get() = if (amountPaid < totalPrice) totalPrice - amountPaid else 0

    // Logika Bisnis: Menentukan status Lunas/Hutang
    val isPaidOff: Boolean
        get() = remainingDebt <= 0

    // Logika Bisnis: Menghitung kembalian
    val changeAmount: Long
        get() = if (amountPaid > totalPrice) amountPaid - totalPrice else 0
}