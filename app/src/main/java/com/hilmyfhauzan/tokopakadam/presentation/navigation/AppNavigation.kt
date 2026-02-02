package com.hilmyfhauzan.tokopakadam.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hilmyfhauzan.tokopakadam.presentation.screens.home.HomeScreen

@Composable
fun AppNavigation(widthSizeClass: WindowWidthSizeClass) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                    widthSizeClass = widthSizeClass,
                    onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable(Screen.History.route) { PlaceholderScreen("History Screen") }
        composable(Screen.Stock.route) { PlaceholderScreen("Stock Screen") }
        composable(Screen.Debt.route) { PlaceholderScreen("Debt Screen") }
        composable(Screen.Settings.route) { PlaceholderScreen("Settings Screen") }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}
