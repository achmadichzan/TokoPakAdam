package com.hilmyfhauzan.tokopakadam.domain.usecase

import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(private val repository: TransactionRepository) {
    // Operator invoke memungkinkan kita memanggil class ini seperti fungsi:
    // getTransactionsUseCase()
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getAllTransactions()
    }
}