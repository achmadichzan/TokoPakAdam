package com.hilmyfhauzan.tokopakadam.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hilmyfhauzan.tokopakadam.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // Ambil semua, urutkan dari yang paling baru
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    // Ambil berdasarkan range tanggal (untuk rekap harian)
    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :startEpoch AND :endEpoch ORDER BY timestamp DESC")
    fun getTransactionsByDateRange(startEpoch: Long, endEpoch: Long): Flow<List<TransactionEntity>>

    // Ambil yang hutang (Total harga > yang dibayar)
    // Query ini cepat karena langsung difilter di SQL
    @Query("SELECT * FROM transactions WHERE amountPaid < totalPrice ORDER BY timestamp DESC")
    fun getUnpaidTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): Flow<TransactionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}