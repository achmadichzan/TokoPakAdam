package com.hilmyfhauzan.tokopakadam.presentation.screen.component

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable

@Composable
fun SideMenu(
    drawerState: DrawerState,
    drawerContent: @Composable (() -> Unit),
    content: @Composable (() -> Unit)
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = drawerContent,
        content = content
    )
}