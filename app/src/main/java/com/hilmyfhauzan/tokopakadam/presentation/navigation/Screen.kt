package com.hilmyfhauzan.tokopakadam.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Stock : Screen("stock")
    data object Debt : Screen("debt")
    data object Settings : Screen("settings")
}
