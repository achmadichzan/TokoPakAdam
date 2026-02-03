package com.hilmyfhauzan.tokopakadam.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.hilmyfhauzan.tokopakadam.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepositoryImpl(private val context: Context) : UserPreferencesRepository {

    private val themeKey = booleanPreferencesKey("is_dark_mode")

    override fun getThemeMode(): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            if (preferences.contains(themeKey)) {
                preferences[themeKey]
            } else {
                null
            }
        }
    }

    override suspend fun setThemeMode(isDarkMode: Boolean?) {
        context.dataStore.edit { preferences ->
            if (isDarkMode == null) {
                preferences.remove(themeKey)
            } else {
                preferences[themeKey] = isDarkMode
            }
        }
    }
}
