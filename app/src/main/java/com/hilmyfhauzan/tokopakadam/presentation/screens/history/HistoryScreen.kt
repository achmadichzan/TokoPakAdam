package com.hilmyfhauzan.tokopakadam.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onMenuClick: () -> Unit, onFabClick: () -> Unit = {}) {
    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    "Riwayat Penjualan",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onMenuClick) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(
                                    onClick = onFabClick,
                                    colors =
                                            IconButtonDefaults.iconButtonColors(
                                                    containerColor = Color(0xFF4285F4),
                                                    contentColor = Color.White
                                            )
                            ) { Icon(Icons.Default.Add, contentDescription = "Add Transaction") }
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color(0xFFF8F9FA)
                                )
                )
            },
            containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                    modifier =
                            Modifier.padding(paddingValues)
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Cari transaksi...", color = Color.Gray) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.White,
                                        focusedContainerColor = Color.White,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = Color(0xFF4285F4)
                                ),
                        singleLine = true
                )

                // Date Filters
                Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text("Hari Ini") },
                            leadingIcon = {
                                Icon(
                                        Icons.Outlined.CalendarToday,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                )
                            },
                            colors =
                                    FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF4285F4),
                                            selectedLabelColor = Color.White,
                                            selectedLeadingIconColor = Color.White
                                    ),
                            shape = RoundedCornerShape(20.dp),
                            border =
                                    FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = true,
                                            borderColor = Color.Transparent
                                    )
                    )
                    FilterChip(
                            selected = false,
                            onClick = {},
                            label = { Text("Kemarin") },
                            colors =
                                    FilterChipDefaults.filterChipColors(
                                            containerColor = Color.White
                                    ),
                            shape = RoundedCornerShape(20.dp),
                            border = null
                    )
                    FilterChip(
                            selected = false,
                            onClick = {},
                            label = { Text("7 Hari Terakhir") },
                            colors =
                                    FilterChipDefaults.filterChipColors(
                                            containerColor = Color.White
                                    ),
                            shape = RoundedCornerShape(20.dp),
                            border = null
                    )
                    IconButton(
                            onClick = { /* TODO: Open Filter */},
                            modifier =
                                    Modifier.background(Color.White, RoundedCornerShape(8.dp))
                                            .size(32.dp)
                    ) {
                        Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filter",
                                modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                        "TERBARU",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        TransactionCard(
                                date = "14 Okt 2023 • 09:45",
                                items = "2 Rak Telur Besar",
                                paymentMethod = "Tunai",
                                amount = "Rp 124.000",
                                status = "LUNAS",
                                statusColor = Color(0xFFE6F4EA),
                                statusTextColor = Color(0xFF137333)
                        )
                    }
                    item {
                        TransactionCard(
                                date = "14 Okt 2023 • 08:30",
                                items = "1/2 Rak Telur Sedang, 10 Tahu",
                                paymentMethod = "Warung Bu Siti",
                                amount = "Rp 45.000",
                                status = "HUTANG",
                                statusColor = Color(0xFFFEF7E0),
                                statusTextColor = Color(0xFFB06000),
                                isDebt = true
                        )
                    }
                    item {
                        TransactionCard(
                                date = "13 Okt 2023 • 17:15",
                                items = "5 Rak Telur Kecil",
                                paymentMethod = "Transfer Bank",
                                amount = "Rp 285.000",
                                status = "LUNAS",
                                statusColor = Color(0xFFE6F4EA),
                                statusTextColor = Color(0xFF137333)
                        )
                    }

                    item {
                        Text(
                                "KEMARIN",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        TransactionCard(
                                date = "12 Okt 2023 • 14:20",
                                items = "1 Rak Telur Besar",
                                paymentMethod = "Tunai",
                                amount = "Rp 62.000",
                                status = "LUNAS",
                                statusColor = Color(0xFFE6F4EA),
                                statusTextColor = Color(0xFF137333)
                        )
                    }
                }
            }

            // Bottom Summary Card (Floating)
            Card(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
                    shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                                "TOTAL PENJUALAN HARI INI",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                                "Rp 169.000",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                            modifier =
                                    Modifier.size(48.dp)
                                            .background(
                                                    Color.White.copy(alpha = 0.2f),
                                                    RoundedCornerShape(12.dp)
                                            ),
                            contentAlignment = Alignment.Center
                    ) {
                        Icon(
                                imageVector = Icons.Default.ShowChart,
                                contentDescription = null,
                                tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(
        date: String,
        items: String,
        paymentMethod: String,
        amount: String,
        status: String,
        statusColor: Color,
        statusTextColor: Color,
        isDebt: Boolean = false
) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Surface(color = statusColor, shape = RoundedCornerShape(4.dp)) {
                    Text(
                            text = status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = statusTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                    text = items,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                            imageVector =
                                    if (isDebt) Icons.Outlined.Person
                                    else Icons.Outlined.CreditCard,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                            text = paymentMethod,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                    )
                }

                Text(
                        text = amount,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4)
                )
            }
        }
    }
}
