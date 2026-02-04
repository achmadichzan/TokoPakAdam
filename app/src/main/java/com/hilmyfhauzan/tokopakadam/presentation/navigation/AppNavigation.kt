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
import com.hilmyfhauzan.tokopakadam.presentation.screen.history.HistoryScreen
import com.hilmyfhauzan.tokopakadam.presentation.screen.main.MainScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(
    modifier: Modifier,
    widthSizeClass: WindowWidthSizeClass
) {
    val nacController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = nacController,
        startDestination = Route.Main
    ) {
        composable<Route.Main> {
            MainScreen(
                widthSizeClass = widthSizeClass,
                onNavigate = { route ->
                    nacController.navigate(route) {
                        popUpTo(nacController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<Route.History> {
            HistoryScreen(
                onNavigate = { route ->
                    nacController.navigate(route) {
                        popUpTo(nacController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<Route.Stock> {
            PlaceholderScreen("Stock")
        }
        composable<Route.Debt> {
            PlaceholderScreen("Debt")
        }
        composable<Route.Settings> {
            PlaceholderScreen("Settings")
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}

sealed interface Route {
    @Serializable object Main : Route
    @Serializable object History : Route
    @Serializable object Stock : Route
    @Serializable object Debt : Route
    @Serializable object Settings : Route
}