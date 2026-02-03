package com.hilmyfhauzan.tokopakadam.domain.usecase

import com.hilmyfhauzan.tokopakadam.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(private val repository: UserPreferencesRepository) {
    operator fun invoke(): Flow<Boolean?> {
        return repository.getThemeMode()
    }
}
