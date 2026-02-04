package com.hilmyfhauzan.tokopakadam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import com.hilmyfhauzan.tokopakadam.presentation.navigation.AppNavigation
import com.hilmyfhauzan.tokopakadam.presentation.ui.theme.TokoPakAdamTheme
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSize = calculateWindowSizeClass(this)
            val themeViewModel: ThemeViewModel = koinViewModel()
            val themeState by themeViewModel.themeState.collectAsStateWithLifecycle()

            TokoPakAdamTheme(
                darkTheme = themeState ?: isSystemInDarkTheme(),
                themePreference = themeState, // Pass the raw nullable state
                onToggleTheme = themeViewModel::toggleTheme
            ) {
                AppNavigation(
                    modifier = Modifier.keepScreenOn(),
                    widthSizeClass = windowSize.widthSizeClass
                )
            }
        }
    }
}
