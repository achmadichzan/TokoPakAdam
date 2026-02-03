package com.hilmyfhauzan.tokopakadam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryTransaction
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionStatus
import com.hilmyfhauzan.tokopakadam.domain.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistoryViewModel(
    getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _currentFilter = MutableStateFlow(HistoryFilter.TODAY)
    val currentFilter: StateFlow<HistoryFilter> = _currentFilter.asStateFlow()

    private val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Source of truth for transactions
    private val allTransactions = getTransactionsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val historyState: StateFlow<List<HistoryTransaction>> = combine(
        allTransactions,
        _currentFilter
    ) { transactions, filter ->
        val filteredTransactions = transactions.filter { transaction ->
            when (filter) {
                HistoryFilter.TODAY -> isToday(transaction.timestamp)
                HistoryFilter.YESTERDAY -> isYesterday(transaction.timestamp)
                HistoryFilter.LAST_7_DAYS -> isLast7Days(transaction.timestamp)
                HistoryFilter.LAST_30_DAYS -> isLast30Days(transaction.timestamp)
                HistoryFilter.ALL -> true
            }
        }
        filteredTransactions.map { it.toHistoryUiModel() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // specific stream for today's total sales, independent of filter
    val totalSalesToday: StateFlow<Long> = allTransactions
        .map { transactions ->
            transactions
                .filter { isToday(it.timestamp) }
                .sumOf { it.totalPrice }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    fun setFilter(filter: HistoryFilter) {
        _currentFilter.value = filter
    }

    private fun Transaction.toHistoryUiModel(): HistoryTransaction {
        val dateObj = Date(timestamp)
        return HistoryTransaction(
            id = id.toString(),
            date = calculateDateString(timestamp),
            time = timeFormatter.format(dateObj),
            items = items.joinToString(", ") { "${it.quantity.toIntOrString()} ${it.productType.displayName}" },
            customerName = customerName,
            paymentMethod = if (amountPaid >= totalPrice) "Tunai" else "Hutang",
            amount = totalPrice,
            cash = amountPaid,
            remainingDebt = remainingDebt,
            change = changeAmount,
            status = if (isPaidOff) TransactionStatus.LUNAS else TransactionStatus.HUTANG
        )
    }

    private fun calculateDateString(timestamp: Long): String {
        return if (isToday(timestamp)) "Hari Ini"
        else if (isYesterday(timestamp)) "Kemarin"
        else dateFormatter.format(Date(timestamp))
    }

    private fun isToday(timestamp: Long): Boolean {
        val now = System.currentTimeMillis()
        return stripTime(timestamp) == stripTime(now)
    }

    private fun isYesterday(timestamp: Long): Boolean {
        val now = System.currentTimeMillis()
        val yesterday = now - (24 * 60 * 60 * 1000)
        return stripTime(timestamp) == stripTime(yesterday)
    }

    private fun isLast7Days(timestamp: Long): Boolean {
        val now = System.currentTimeMillis()
        val sevenDaysAgo = now - (6 * 24 * 60 * 60 * 1000) // 6 days ago + today = 7 days
        return stripTime(timestamp) >= stripTime(sevenDaysAgo)
    }

    private fun isLast30Days(timestamp: Long): Boolean {
        val now = System.currentTimeMillis()
        val thirtyDaysAgo = now - (29L * 24 * 60 * 60 * 1000) // 29 days ago + today = 30 days
        return stripTime(timestamp) >= stripTime(thirtyDaysAgo)
    }

    private fun stripTime(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun Double.toIntOrString(): String {
        return if (this % 1.0 == 0.0) this.toLong().toString() else this.toString()
    }
}

enum class HistoryFilter {
    TODAY, YESTERDAY, LAST_7_DAYS, LAST_30_DAYS, ALL
}
