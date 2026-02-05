package com.hilmyfhauzan.tokopakadam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmyfhauzan.tokopakadam.domain.model.DebtUiModel
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryFilter
import com.hilmyfhauzan.tokopakadam.domain.usecase.GetTransactionsUseCase
import com.hilmyfhauzan.tokopakadam.domain.usecase.UpdateTransactionUseCase
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryTransaction
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionStatus
import kotlinx.coroutines.launch
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

class DebtViewModel(
    getTransactionsUseCase: GetTransactionsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    private val _currentFilter = MutableStateFlow(HistoryFilter.ALL) // Default to ALL for debts
    val currentFilter: StateFlow<HistoryFilter> = _currentFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val dateFormatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.forLanguageTag("id-ID")
    )
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val allTransactions = getTransactionsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val debtState: StateFlow<List<DebtUiModel>> = combine(
        allTransactions,
        _currentFilter,
        _searchQuery
    ) { transactions, filter, query ->
        // 1. Filter by Date first
        val dateFiltered = transactions.filter { transaction ->
            if (transaction.isPaidOff) return@filter false // Only unpaid
            
            when (filter) {
                HistoryFilter.TODAY -> isToday(transaction.timestamp)
                HistoryFilter.YESTERDAY -> isYesterday(transaction.timestamp)
                HistoryFilter.LAST_7_DAYS -> isLast7Days(transaction.timestamp)
                HistoryFilter.LAST_30_DAYS -> isLast30Days(transaction.timestamp)
                HistoryFilter.ALL -> true
            }
        }

        // 2. Group by Customer
        val grouped = dateFiltered.groupBy { it.customerName ?: "Pelanggan Umum" }

        // 3. Transform to UI Model and Filter by Search Query
        grouped.map { (name, customerTransactions) ->
            val totalDebt = customerTransactions.sumOf { it.remainingDebt }
            val lastTransaction = customerTransactions.maxByOrNull { it.timestamp }
            val lastDate = lastTransaction?.timestamp ?: 0L
            
            // Aggregate Items
            // Collect all items from all unpaid transactions
            val allItems = customerTransactions.flatMap { it.items }
            // Group by product name and sum quantity
            val aggregatedItems = allItems.groupBy { it.productType.displayName }
                .map { (productName, items) ->
                     val totalQty = items.sumOf { it.quantity }
                     // Assuming quantity is Double, strict to int format if whole number
                     val qtyString =
                         if (totalQty % 1.0 == 0.0) totalQty.toLong().toString()
                         else totalQty.toString()
                     "$qtyString $productName"
                }

            // Collect Notes
            val notes = customerTransactions.mapNotNull { it.note }.filter { it.isNotBlank() }
            
            val historyTransactions = customerTransactions.map { it.toHistoryUiModel() }

            DebtUiModel(
                customerName = name,
                totalDebt = totalDebt,
                lastTransactionDate = lastDate,
                lastTransactionDateString = "Terakhir: " + dateFormatter.format(Date(lastDate)),
                debtItems = aggregatedItems,
                notes = notes,
                unpaidTransactions = historyTransactions
            )
        }.filter { uiModel ->
            val queryLower = query.lowercase()
            uiModel.customerName.lowercase().contains(queryLower) ||
                    uiModel.notes.any { it.lowercase().contains(queryLower) }
        }.sortedByDescending { it.lastTransactionDate } // Sort by most recent interaction

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalDebtAmount: StateFlow<Long> = debtState.map { list ->
        list.sumOf { it.totalDebt }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0L
    )

    val totalDebtPeople: StateFlow<Int> = debtState.map { list ->
        list.size
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun setFilter(filter: HistoryFilter) {
        _currentFilter.value = filter
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
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
        val sevenDaysAgo = now - (6 * 24 * 60 * 60 * 1000)
        return stripTime(timestamp) >= stripTime(sevenDaysAgo)
    }

    private fun isLast30Days(timestamp: Long): Boolean {
        val now = System.currentTimeMillis()
        val thirtyDaysAgo = now - (29L * 24 * 60 * 60 * 1000)
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

    fun updateTransaction(id: String, newCustomerName: String, newCash: Long, newNote: String) {
        viewModelScope.launch {
            val transactionId = id.toLongOrNull() ?: return@launch
            val currentTransaction = allTransactions.value.find { it.id == transactionId } ?: return@launch
            
            val updatedTransaction = currentTransaction.copy(
                customerName = newCustomerName,
                amountPaid = newCash,
                note = newNote
            )
            updateTransactionUseCase(updatedTransaction)
        }
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
            status = if (isPaidOff) TransactionStatus.LUNAS else TransactionStatus.HUTANG,
            note = note
        )
    }

    private fun calculateDateString(timestamp: Long): String {
        return if (isToday(timestamp)) "Hari Ini"
        else if (isYesterday(timestamp)) "Kemarin"
        else dateFormatter.format(Date(timestamp))
    }

    private fun Double.toIntOrString(): String {
        return if (this % 1.0 == 0.0) this.toLong().toString() else this.toString()
    }
}
