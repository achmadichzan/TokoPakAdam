package com.hilmyfhauzan.tokopakadam.domain.repository

import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    /**
     * Mengambil semua riwayat transaksi, diurutkan dari yang terbaru.
     * Return Flow agar UI selalu update otomatis (Reactive).
     * Digunakan di Screen: Riwayat Penjualan
     */
    fun getAllTransactions(): Flow<List<Transaction>>

    /**
     * Mengambil transaksi berdasarkan rentang waktu (misal: Hari Ini).
     * Digunakan untuk menghitung "Total Penjualan Hari Ini".
     */
    fun getTransactionsByDateRange(startEpoch: Long, endEpoch: Long): Flow<List<Transaction>>

    /**
     * Khusus mengambil transaksi yang belum lunas (Hutang > 0).
     * Digunakan di Screen: Daftar Hutang
     */
    fun getUnpaidTransactions(): Flow<List<Transaction>>

    /**
     * Mengambil detail satu transaksi.
     * Berguna jika nanti ada halaman detail struk atau edit transaksi.
     */
    fun getTransactionById(id: Long): Flow<Transaction?>

    /**
     * Menyimpan transaksi baru.
     * Mengembalikan ID transaksi yang baru dibuat.
     * Digunakan saat tombol "SIMPAN" ditekan.
     */
    suspend fun insertTransaction(transaction: Transaction): Long

    /**
     * Mengupdate data transaksi.
     * Digunakan saat pelanggan membayar hutang (update amountPaid).
     */
    suspend fun updateTransaction(transaction: Transaction)

    /**
     * Menghapus transaksi (opsional, untuk admin/owner).
     */
    suspend fun deleteTransaction(transaction: Transaction)
}