package com.hilmyfhauzan.tokopakadam.data.repository

import com.hilmyfhauzan.tokopakadam.data.local.dao.TransactionDao
import com.hilmyfhauzan.tokopakadam.data.mapper.toDomain
import com.hilmyfhauzan.tokopakadam.data.mapper.toEntity
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction
import com.hilmyfhauzan.tokopakadam.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        // Mapping dari Flow<List<Entity>> ke Flow<List<Domain>>
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByDateRange(startEpoch: Long, endEpoch: Long): Flow<List<Transaction>> {
        return dao.getTransactionsByDateRange(startEpoch, endEpoch).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUnpaidTransactions(): Flow<List<Transaction>> {
        return dao.getUnpaidTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionById(id: Long): Flow<Transaction?> {
        return dao.getTransactionById(id).map { it?.toDomain() }
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return dao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction.toEntity())
    }
}