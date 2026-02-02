package com.hilmyfhauzan.tokopakadam.presentation.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Long): String {
    // Gunakan Locale.forLanguageTag untuk pengganti konstruktor yang deprecated
    val localeID = Locale.forLanguageTag("id-ID")

    // Ambil simbol format dari locale Indonesia
    val symbols = DecimalFormatSymbols(localeID).apply {
        // Set simbol mata uang secara manual dengan tambahan spasi
        currencySymbol = "Rp "
    }

    // Gunakan DecimalFormat dengan symbols yang sudah dimodifikasi
    val formatRupiah = (NumberFormat.getCurrencyInstance(localeID) as DecimalFormat).apply {
        decimalFormatSymbols = symbols
        maximumFractionDigits = 0
    }

    return formatRupiah.format(amount)
}