package com.hilmyfhauzan.tokopakadam.presentation.screen.component

import com.hilmyfhauzan.tokopakadam.presentation.ui.theme.LocalThemePreference
import com.hilmyfhauzan.tokopakadam.presentation.ui.theme.LocalThemeToggle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTopBar(
    title: String,
    onDrawerClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val themePreference = LocalThemePreference.current
    val onToggleTheme = LocalThemeToggle.current

    Row(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onDrawerClick) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Row {
            if (title == "Toko Pak Adam") {
                Text(
                    text = "Toko ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pak Adam",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onToggleTheme) {
                val icon = when (themePreference) {
                    true -> Icons.Default.Nightlight
                    false -> Icons.Default.LightMode
                    null -> Icons.Default.BrightnessAuto
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "Theme Toggle",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            actions()
        }
    }
}
