package com.hilmyfhauzan.tokopakadam.presentation.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryTransaction
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionStatus
import com.hilmyfhauzan.tokopakadam.presentation.navigation.Route
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.SideMenu
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.AppTopBar
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.DrawerContent
import kotlinx.coroutines.launch

val mockTransactionsStart = listOf(
    HistoryTransaction(
        id = "1",
        date = "Hari Ini",
        time = "09:45",
        items = "2 Rak Telur Besar",
        customerName = null,
        paymentMethod = "Tunai",
        amount = 124000L,
        status = TransactionStatus.LUNAS
    ),
    HistoryTransaction(
        id = "2",
        date = "Hari Ini",
        time = "08:30",
        items = "1/2 Rak Telur Sedang, 10 Tahu",
        customerName = "Warung Bu Siti",
        paymentMethod = "Tunai",
        amount = 45000L,
        status = TransactionStatus.HUTANG
    ),
    HistoryTransaction(
        id = "3",
        date = "Hari Ini",
        time = "17:15",
        items = "5 Rak Telur Kecil",
        customerName = null,
        paymentMethod = "Transfer Bank",
        amount = 285000L,
        status = TransactionStatus.LUNAS
    )
)

val mockTransactionsYesterday = listOf(
    HistoryTransaction(
        id = "4",
        date = "Kemarin",
        time = "14:20",
        items = "1 Rak Telur Besar",
        customerName = null,
        paymentMethod = "Tunai",
        amount = 62000L,
        status = TransactionStatus.LUNAS
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onNavigate: (Route) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SideMenu(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = Route.History,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    onNavigate(route)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Riwayat Penjualan",
                    onDrawerClick = { scope.launch { drawerState.open() } },
                    actions = {
                        IconButton(onClick = { /* TODO: Add Transaction */ }) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MaterialTheme.colorScheme.secondary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                // Optional: Floating Action Button if needed, but design shows bottom card
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SearchBarSection()
                    FilterSection()
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        item {
                            SectionHeader(title = "TERBARU")
                        }
                        items(mockTransactionsStart) { transaction ->
                            TransactionItemCard(transaction)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            SectionHeader(title = "KEMARIN")
                        }
                        items(mockTransactionsYesterday) { transaction ->
                            TransactionItemCard(transaction)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                BottomSummaryCard(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}