package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumpadSection(
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onHalfTrayClick: () -> Unit,
    onOneTrayClick: () -> Unit
) {
    val buttons = listOf(
        "1", "2", "3", "BACK",
        "4", "5", "6", "CLEAR",
        "7", "8", "9", "00",
        "0", "000", "0.5 RAK", "RAK"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(260.dp) // Fixed height for grid
    ) {
        items(buttons) { btn ->
            NumpadButton(
                text = btn,
                onClick = {
                    when (btn) {
                        "BACK" -> onBackspaceClick()
                        "CLEAR" -> onClearClick()
                        "0.5 RAK" -> onHalfTrayClick()
                        "RAK" -> onOneTrayClick()
                        else -> onNumberClick(btn)
                    }
                }
            )
        }
    }
}

@Composable
fun NumpadButton(text: String, onClick: () -> Unit) {
    val isSpecial = text == "0.5 RAK" || text == "RAK"
    val isClear = text == "CLEAR"
    val isBack = text == "BACK"

    val containerColor = if (isSpecial) Color(0xFFFFE0B2) // Light Orange
    else if (isClear) Color(0xFFFFEBEE) // Light Red
    else if (isBack) Color(0xFFE3F2FD) // Light Blue
    else Color.White

    val contentColor = if (isSpecial) Color(0xFFE65100)
    else if (isClear) DangerRed
    else if (isBack) TextBlack
    else TextBlack

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        modifier = Modifier.height(56.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                isBack -> Icon(Icons.AutoMirrored.Rounded.Backspace, contentDescription = "Back", tint = Color(0xFF546E7A))
                isClear -> Icon(Icons.Default.Close, contentDescription = "Clear", tint = DangerRed)
                isSpecial -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (text.contains("0.5")) {
                        Text("0.5", fontSize = 12.sp, color = contentColor, fontWeight = FontWeight.Bold)
                        Text("1/2 RAK", fontSize = 10.sp, color = contentColor)
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
                        Text("RAK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = contentColor)
                    }
                }
                else -> Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = contentColor)
            }
        }
    }
}