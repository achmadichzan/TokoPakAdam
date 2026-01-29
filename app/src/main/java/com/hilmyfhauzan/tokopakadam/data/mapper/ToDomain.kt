package com.hilmyfhauzan.tokopakadam.data.mapper

import com.hilmyfhauzan.tokopakadam.data.local.converters.TransactionConverters
import com.hilmyfhauzan.tokopakadam.data.local.entity.TransactionEntity
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction

internal val converter = TransactionConverters()

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        timestamp = timestamp,
        customerName = customerName,
        items = converter.toTransactionItemList(itemsJson),
        totalPrice = totalPrice,
        amountPaid = amountPaid,
        note = note
    )
}