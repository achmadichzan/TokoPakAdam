package com.hilmyfhauzan.tokopakadam.presentation.screen.debt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilmyfhauzan.tokopakadam.domain.model.DebtUiModel
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryTransaction
import com.hilmyfhauzan.tokopakadam.presentation.navigation.Route
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.AppTopBar
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.DrawerContent
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.SideMenu
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.TransactionDetailDialog
import com.hilmyfhauzan.tokopakadam.presentation.screen.history.FilterSection
import com.hilmyfhauzan.tokopakadam.presentation.screen.history.SearchBarSection
import com.hilmyfhauzan.tokopakadam.presentation.util.formatRupiah
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.DebtViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtScreen(
    viewModel: DebtViewModel = koinViewModel(),
    onNavigate: (Route) -> Unit
) {
    val debtState by viewModel.debtState.collectAsStateWithLifecycle()
    val totalDebtAmount by viewModel.totalDebtAmount.collectAsStateWithLifecycle()
    val totalDebtPeople by viewModel.totalDebtPeople.collectAsStateWithLifecycle()
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedTransaction by remember { mutableStateOf<HistoryTransaction?>(null) }
    var isTransactionSelectorOpen by remember { mutableStateOf(false) }
    var selectedDebtForSelection by remember { mutableStateOf<DebtUiModel?>(null) }

    SideMenu(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = Route.Debt,
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
                    title = "Daftar Hutang",
                    onDrawerClick = { scope.launch { drawerState.open() } },
                    actions = {
                        IconButton(onClick = { onNavigate(Route.Main) }) {
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
                    var isSearchExpanded by remember { mutableStateOf(false) }

                    if (isSearchExpanded) {
                        SearchBarSection(
                            query = searchQuery,
                            onQueryChange = viewModel::onSearchQueryChange,
                            onClose = {
                                viewModel.onSearchQueryChange("")
                                isSearchExpanded = false
                            }
                        )
                    } else {
                         Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterSection(
                                currentFilter = currentFilter,
                                onFilterSelected = viewModel::setFilter,
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Search Icon Chip
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                onClick = { isSearchExpanded = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            DebtSummaryCard(
                                totalDebt = totalDebtAmount,
                                debtCount = totalDebtPeople
                            )
                        }

                        if (debtState.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tidak ada daftar hutang",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            items(debtState) { debt ->
                                DebtItemCard(
                                    debt = debt,
                                    onPayClick = { 
                                        if (debt.unpaidTransactions.size == 1) {
                                            selectedTransaction = debt.unpaidTransactions.first()
                                        } else if (debt.unpaidTransactions.isNotEmpty()) {
                                            selectedDebtForSelection = debt
                                            isTransactionSelectorOpen = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            // Transaction Detail Dialog
            if (selectedTransaction != null) {
                TransactionDetailDialog(
                    transaction = selectedTransaction!!,
                    onDismiss = { selectedTransaction = null },
                    onSave = { name, cash, note ->
                        viewModel.updateTransaction(
                            id = selectedTransaction!!.id,
                            newCustomerName = name,
                            newCash = cash,
                            newNote = note
                        )
                        selectedTransaction = null
                    }
                )
            }

            // Transaction Selector Dialog
            if (isTransactionSelectorOpen && selectedDebtForSelection != null) {
                AlertDialog(
                    onDismissRequest = { isTransactionSelectorOpen = false },
                    title = { Text(
                        text = "Pilih Transaksi",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    ) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            selectedDebtForSelection!!.unpaidTransactions.forEach { tx ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedTransaction = tx
                                            isTransactionSelectorOpen = false
                                        }
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = tx.date,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = tx.items,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }
                                    Text(
                                        text = formatRupiah(tx.remainingDebt),
                                        style = MaterialTheme.typography.bodyMedium
                                            .copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { isTransactionSelectorOpen = false }) {
                            Text("Batal")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}
