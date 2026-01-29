package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SideMenu(
        modifier: Modifier = Modifier,
        onHistoryClick: () -> Unit = {},
        onDebtClick: () -> Unit = {}
) {
        // ... rest of code (same)
        NavigationRail(
                modifier = modifier.fillMaxHeight(),
        ) {
                Spacer(modifier = Modifier.weight(1f))

                NavigationRailItem(
                        selected = false, // TODO: Manage selection state
                        onClick = onHistoryClick,
                        icon = { Icon(Icons.Default.History, contentDescription = "Riwayat") },
                        label = { Text("Riwayat") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                NavigationRailItem(
                        selected = false,
                        onClick = onDebtClick,
                        icon = {
                                Icon(
                                        Icons.Default.AccountBalanceWallet,
                                        contentDescription = "Hutang"
                                )
                        },
                        label = { Text("Hutang") }
                )

                Spacer(modifier = Modifier.weight(1f))
        }
}
