package com.hilmyfhauzan.tokopakadam.domain.model

data class HistoryTransaction(
    val id: String,
    val date: String,
    val time: String,
    val items: String,
    val customerName: String?,
    val paymentMethod: String, // "Tunai" or "Transfer Bank"
    val amount: Long,
    val cash: Long,
    val remainingDebt: Long,
    val change: Long,
    val status: TransactionStatus,
    val note: String?
)