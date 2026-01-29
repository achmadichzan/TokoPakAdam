package com.hilmyfhauzan.tokopakadam.data.mapper

import com.hilmyfhauzan.tokopakadam.data.local.entity.TransactionEntity
import com.hilmyfhauzan.tokopakadam.domain.model.Transaction

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        timestamp = timestamp,
        customerName = customerName,
        itemsJson = converter.fromTransactionItemList(items),
        totalPrice = totalPrice,
        amountPaid = amountPaid,
        note = note
    )
}