package com.hilmyfhauzan.tokopakadam.domain.model

data class DebtUiModel(
    val customerName: String,
    val totalDebt: Long,
    val lastTransactionDate: Long,
    val lastTransactionDateString: String,
    val debtItems: List<String>, // Summary of items e.g. "15 Telur Kecil"
    val notes: List<String>, // Collected notes from unpaid transactions
    val unpaidTransactions: List<HistoryTransaction> // For dialog usage
)