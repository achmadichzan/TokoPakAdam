package com.hilmyfhauzan.tokopakadam.domain.usecase

import com.hilmyfhauzan.tokopakadam.domain.repository.UserPreferencesRepository

class SetThemeUseCase(private val repository: UserPreferencesRepository) {
    suspend operator fun invoke(isDarkMode: Boolean?) {
        repository.setThemeMode(isDarkMode)
    }
}
