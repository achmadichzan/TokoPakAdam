package com.hilmyfhauzan.tokopakadam.presentation.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilmyfhauzan.tokopakadam.presentation.navigation.Route
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.AppTopBar
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.DrawerContent
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.SideMenu
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.HistoryViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel(),
    onNavigate: (Route) -> Unit
) {
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()
    val totalSalesToday by viewModel.totalSalesToday.collectAsStateWithLifecycle()
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState =  rememberLazyListState()
    var isBottomBarVisible by remember { mutableStateOf(true) }

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
                    SearchBarSection(
                        query = searchQuery,
                        onQueryChange = viewModel::onSearchQueryChange
                    )
                    FilterSection(
                        currentFilter = currentFilter,
                        onFilterSelected = viewModel::setFilter
                    )
                    
                    if (historyState.isEmpty()) {
                         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                             Text("Belum ada riwayat transaksi")
                         }
                    } else {
                        LaunchedEffect(listState) {
                            snapshotFlow {
                                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
                            }
                                .distinctUntilChanged()
                                // Menggunakan runningFold untuk menyimpan "state sebelumnya" (index, offset)
                                .runningFold(Pair(0, 0)) { previous, current ->
                                    val (lastIndex, lastScroll) = previous
                                    val (currentIndex, currentScroll) = current

                                    val isAtTop = currentIndex == 0 && currentScroll == 0
                                    val isScrollingDown =
                                        currentIndex > lastIndex || (currentIndex ==
                                            lastIndex && currentScroll > lastScroll)

                                    isBottomBarVisible = when {
                                        isAtTop -> true
                                        isScrollingDown -> false
                                        else -> true // Scroll Up
                                    }

                                    current // Kembalikan nilai saat ini untuk jadi 'previous' di iterasi berikutnya
                                }
                                .collect()
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            val groupedTransactions =
                                historyState.groupBy { transaction -> transaction.date }
                            
                            groupedTransactions.forEach { (date, transactions) ->
                                item {
                                    SectionHeader(title = date.uppercase())
                                }
                                items(transactions) { transaction ->
                                    TransactionItemCard(
                                        transaction = transaction,
                                        onUpdate = viewModel::updateTransaction
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    BottomSummaryCard(
                        totalSales = totalSalesToday,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}