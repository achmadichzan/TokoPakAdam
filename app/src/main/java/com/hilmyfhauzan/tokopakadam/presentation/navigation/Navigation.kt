package com.hilmyfhauzan.tokopakadam.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.hilmyfhauzan.tokopakadam.presentation.screens.history.HistoryScreen
import com.hilmyfhauzan.tokopakadam.presentation.screens.home.HomeScreen
import com.hilmyfhauzan.tokopakadam.presentation.screens.home.SideMenu
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// Navigation keys implementing NavKey
@Serializable data object Home : NavKey

@Serializable data object History : NavKey

@Composable
fun AppNavigation(widthSizeClass: WindowWidthSizeClass) {
    val backStack = rememberNavBackStack(Home)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle Back Press
    BackHandler(enabled = backStack.size > 1) { backStack.removeLastOrNull() }

    ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    SideMenu(
                            onHistoryClick = {
                                scope.launch { drawerState.close() }
                                if (backStack.lastOrNull() != History) {
                                    backStack.add(History)
                                }
                            },
                            onDebtClick = {
                                scope.launch { drawerState.close() }
                                // TODO: Navigate to Debt screen
                            }
                    )
                }
            }
    ) {
        NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider =
                        entryProvider {
                            entry<Home> {
                                HomeScreen(
                                        widthSizeClass = widthSizeClass,
                                        onMenuClick = { scope.launch { drawerState.open() } }
                                )
                            }
                            entry<History> {
                                HistoryScreen(onMenuClick = { scope.launch { drawerState.open() } })
                            }
                        }
        )
    }
}
