package com.hilmyfhauzan.tokopakadam.domain.usecase

import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.repository.TransactionRepository

class InsertTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction): Long {
        // Di sini bisa ditambahkan validasi bisnis
        // Contoh: if (transaction.items.isEmpty()) throw Exception("Keranjang kosong")
        return repository.insertTransaction(transaction)
    }
}