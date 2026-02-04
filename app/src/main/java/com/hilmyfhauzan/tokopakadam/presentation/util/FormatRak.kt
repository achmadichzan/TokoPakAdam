package com.hilmyfhauzan.tokopakadam.presentation.util

import kotlin.math.abs
import kotlin.math.floor

fun formatRak(qty: Double): String {
    val rak = qty / 30.0
    val rakInt = rak.toInt()
    val remainder = rak - rakInt

    val result = when {
        // Angka bulat sempurna
        remainder == 0.0 -> rakInt.toString()

        // Kasus 1/2 rak (0.5)
        abs(remainder - 0.5) < 0.001 -> if (rakInt == 0) "1/2" else "$rakInt 1/2"

        // Kasus desimal lainnya (potong ke bawah)
        else -> {
            // Geser koma ke kanan, potong dengan floor, geser kembali ke kiri
            val truncated = floor(rak * 10) / 10.0
            // Format manual ke string dengan koma
            truncated.toString().replace('.', ',')
        }
    }

    return "$result Rak"
}