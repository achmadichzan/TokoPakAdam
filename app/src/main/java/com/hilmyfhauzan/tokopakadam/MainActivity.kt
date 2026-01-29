package com.hilmyfhauzan.tokopakadam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.hilmyfhauzan.tokopakadam.presentation.screens.home.HomeScreen
import com.hilmyfhauzan.tokopakadam.presentation.ui.theme.TokoPakAdamTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSize = calculateWindowSizeClass(this)
            TokoPakAdamTheme { HomeScreen(widthSizeClass = windowSize.widthSizeClass) }
        }
    }
}
