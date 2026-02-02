package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hilmyfhauzan.tokopakadam.presentation.navigation.Screen

@Composable
fun DrawerContent(currentRoute: String = Screen.Home.route, onNavigate: (String) -> Unit) {
    ModalDrawerSheet {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                    text = "Toko Pak Adam",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
            )

            DrawerItem(
                    icon = Icons.Default.Home,
                    label = "Beranda",
                    isSelected = currentRoute == Screen.Home.route,
                    onClick = { onNavigate(Screen.Home.route) }
            )
            DrawerItem(
                    icon = Icons.Default.History,
                    label = "Riwayat",
                    isSelected = currentRoute == Screen.History.route,
                    onClick = { onNavigate(Screen.History.route) }
            )
            DrawerItem(
                    icon = Icons.Default.Inventory,
                    label = "Stok",
                    isSelected = currentRoute == Screen.Stock.route,
                    onClick = { onNavigate(Screen.Stock.route) }
            )
            DrawerItem(
                    icon = Icons.Default.MoneyOff,
                    label = "Hutang",
                    isSelected = currentRoute == Screen.Debt.route,
                    onClick = { onNavigate(Screen.Debt.route) }
            )
            DrawerItem(
                    icon = Icons.Default.Settings,
                    label = "Pengaturan",
                    isSelected = currentRoute == Screen.Settings.route,
                    onClick = { onNavigate(Screen.Settings.route) }
            )
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clickable(onClick = onClick)
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                            .background(
                                    if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.5f
                                            )
                                    else Color.Transparent,
                                    shape = MaterialTheme.shapes.small
                            ),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black
        )
    }
}
