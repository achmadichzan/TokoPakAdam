package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
        onOneTrayClick: () -> Unit,
        onSave: () -> Unit = {},
        isTablet: Boolean = false,
        modifier: Modifier = Modifier
) {
        val buttons =
                if (isTablet) {
                        listOf(
                                "7",
                                "8",
                                "9",
                                "⌫",
                                "4",
                                "5",
                                "6",
                                "CE",
                                "1",
                                "2",
                                "3",
                                "1/2 RAK",
                                "00",
                                "0",
                                "000",
                                "RAK"
                        )
                } else {
                        listOf(
                                "7",
                                "8",
                                "9",
                                "BACK",
                                "4",
                                "5",
                                "6",
                                "CE",
                                "1",
                                "2",
                                "3",
                                "1/2 RAK",
                                "00",
                                "0",
                                "000",
                                "RAK"
                        )
                }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val rows = buttons.chunked(4) // 4x4 Grid -> 4 items per row
                rows.forEach { rowItems ->
                        Row(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                                rowItems.forEach { btn ->
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                                                NumpadButton(
                                                        text = btn,
                                                        onClick = {
                                                                when (btn) {
                                                                        "BACK", "⌫" ->
                                                                                onBackspaceClick()
                                                                        "CLEAR", "CE" ->
                                                                                onClearClick()
                                                                        "0.5 RAK", "1/2 RAK" ->
                                                                                onHalfTrayClick()
                                                                        "RAK" -> onOneTrayClick()
                                                                        "ENTER" -> onSave()
                                                                        else -> onNumberClick(btn)
                                                                }
                                                        }
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
fun NumpadButton(text: String, onClick: () -> Unit) {
        val isSpecial = text == "0.5 RAK" || text == "RAK" || text == "1/2 RAK"
        val isClear = text == "CLEAR" || text == "CE"
        val isBack = text == "BACK" || text == "⌫"
        val isEnter = text == "ENTER"

        val containerColor =
                if (isEnter) Color(0xFFF57C00) // Orange for Enter
                else if (isSpecial) Color(0xFFFFE0B2) // Light Orange
                else if (isClear) Color(0xFFFFEBEE) // Light Red
                else if (isBack) Color(0xFFE3F2FD) // Light Blue
                else Color.White

        val contentColor =
                if (isEnter) Color.White
                else if (isSpecial) Color(0xFFE65100)
                else if (isClear) DangerRed else if (isBack) TextBlack else TextBlack

        Card(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                border =
                        if (!isEnter) BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                        else null,
                modifier = Modifier.fillMaxSize() // Fill grid cell
        ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when {
                                isBack ->
                                        Icon(
                                                Icons.AutoMirrored.Rounded.Backspace,
                                                contentDescription = "Back",
                                                tint = Color(0xFF546E7A)
                                        )
                                isClear -> {
                                        if (text == "CE") {
                                                Text(
                                                        "CE",
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DangerRed
                                                )
                                        } else {
                                                Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = "Clear",
                                                        tint = DangerRed
                                                )
                                        }
                                }
                                isSpecial ->
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                if (text.contains("0.5") || text.contains("1/2")) {
                                                        Text(
                                                                "1/2 RAK",
                                                                fontSize = 12.sp,
                                                                color = contentColor,
                                                                fontWeight = FontWeight.Bold
                                                        )
                                                } else {
                                                        Icon(
                                                                Icons.Default.Check,
                                                                contentDescription = null,
                                                                tint = contentColor,
                                                                modifier = Modifier.size(16.dp)
                                                        )
                                                        Text(
                                                                "RAK",
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = contentColor
                                                        )
                                                }
                                        }
                                isEnter ->
                                        Text(
                                                "ENTER",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                else ->
                                        Text(
                                                text = text,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = contentColor
                                        )
                        }
                }
        }
}
