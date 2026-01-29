package com.hilmyfhauzan.tokopakadam.data.local.converters

import androidx.room.TypeConverter
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionItem
import kotlinx.serialization.json.Json

class TransactionConverters {

    // Konfigurasi Json agar lebih fleksibel (opsional tapi recommended)
    private val json = Json {
        ignoreUnknownKeys = true // Mencegah crash jika nanti struktur JSON berubah
        encodeDefaults = true    // Menyertakan nilai default saat save
    }

    @TypeConverter
    fun fromTransactionItemList(value: List<TransactionItem>): String {
        // Mengubah List Object -> JSON String
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toTransactionItemList(value: String): List<TransactionItem> {
        // Mengubah JSON String -> List Object
        // Tidak perlu TypeToken seperti Gson, Kotlin Serialization membacanya otomatis
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Fallback jika data rusak/null
        }
    }
}