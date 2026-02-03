package com.hilmyfhauzan.tokopakadam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmyfhauzan.tokopakadam.domain.usecase.GetThemeUseCase
import com.hilmyfhauzan.tokopakadam.domain.usecase.SetThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    val themeState: StateFlow<Boolean?> = getThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleTheme() {
        viewModelScope.launch {
            val current = themeState.value
            val next = when (current) {
                null -> false // System -> Light
                false -> true // Light -> Dark
                true -> null  // Dark -> System
            }
            setThemeUseCase(next)
        }
    }
}
