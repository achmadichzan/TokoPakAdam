package com.hilmyfhauzan.tokopakadam.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getThemeMode(): Flow<Boolean?>
    suspend fun setThemeMode(isDarkMode: Boolean?)
}
