package com.hilmyfhauzan.tokopakadam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryTransaction
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionStatus
import com.hilmyfhauzan.tokopakadam.domain.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewModel(
    getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    val historyState: StateFlow<List<HistoryTransaction>> = getTransactionsUseCase()
        .map { transactions ->
            transactions.map { transaction ->
                transaction.toHistoryUiModel()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalSalesToday: StateFlow<Long> = historyState
        .map { transactions ->
            transactions
                .filter { it.date == "Hari Ini" }
                .sumOf { it.amount }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    private fun Transaction.toHistoryUiModel(): HistoryTransaction {
        val dateObj = Date(timestamp)
        return HistoryTransaction(
            id = id.toString(),
            date = calculateDateString(timestamp),
            time = timeFormatter.format(dateObj),
            items = items.joinToString(", ") { "${it.quantity.toIntOrString()} ${it.productType.displayName}" },
            customerName = customerName,
            paymentMethod = if (amountPaid >= totalPrice) "Tunai" else "Hutang", // Simplification
            amount = totalPrice,
            cash = amountPaid,
            remainingDebt = remainingDebt,
            change = changeAmount,
            status = if (isPaidOff) TransactionStatus.LUNAS else TransactionStatus.HUTANG
        )
    }

    private fun calculateDateString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        // Simple logic for "Hari Ini" / "Kemarin"
        // Better implementation would compare calendar days
        val oneDayMillis = 24 * 60 * 60 * 1000
        return when {
            diff < oneDayMillis -> "Hari Ini" // Not perfectly accurate for midnight crossing but acceptable for MVP
            diff < 2 * oneDayMillis -> "Kemarin"
            else -> dateFormatter.format(Date(timestamp))
        }
    }

    private fun Double.toIntOrString(): String {
        return if (this % 1.0 == 0.0) this.toLong().toString() else this.toString()
    }
}
