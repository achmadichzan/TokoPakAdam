package com.hilmyfhauzan.tokopakadam.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hilmyfhauzan.tokopakadam.data.local.converters.TransactionConverters
import com.hilmyfhauzan.tokopakadam.data.local.dao.TransactionDao
import com.hilmyfhauzan.tokopakadam.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true // Ubah true jika ingin export skema JSON
)
// Jangan lupa daftarkan Converters di sini
@TypeConverters(TransactionConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}