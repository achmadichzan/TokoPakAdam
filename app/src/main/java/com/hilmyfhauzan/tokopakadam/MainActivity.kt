package com.hilmyfhauzan.tokopakadam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hilmyfhauzan.tokopakadam.presentation.screens.home.HomeScreen
import com.hilmyfhauzan.tokopakadam.presentation.ui.theme.TokoPakAdamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TokoPakAdamTheme { HomeScreen() }
        }
    }
}