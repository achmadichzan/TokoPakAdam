package com.hilmyfhauzan.tokopakadam.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* TODO: Open Drawer */ }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextBlack)
        }

        // Title with Colored Span simulation
        Row {
            Text(
                text = "Toko ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )
            Text(
                text = "Pak Adam",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )
        }

        IconButton(onClick = { /* TODO: Toggle Dark Mode */ }) {
            Icon(Icons.Default.Nightlight, contentDescription = "Dark Mode", tint = TextBlack)
        }
    }
}