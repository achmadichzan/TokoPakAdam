package com.hilmyfhauzan.tokopakadam.presentation.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Long): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    formatRupiah.maximumFractionDigits = 0
    return formatRupiah.format(amount).replace("Rp", "Rp ")
}