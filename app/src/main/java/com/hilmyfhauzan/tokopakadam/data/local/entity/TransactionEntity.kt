package com.hilmyfhauzan.tokopakadam.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val customerName: String?,
    val itemsJson: String, // Data belanjaan disimpan sebagai String JSON
    val totalPrice: Long,
    val amountPaid: Long,
    val note: String?
)