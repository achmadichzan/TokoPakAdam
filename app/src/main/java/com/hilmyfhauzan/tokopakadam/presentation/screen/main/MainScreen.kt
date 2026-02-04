package com.hilmyfhauzan.tokopakadam.presentation.screen.main

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilmyfhauzan.tokopakadam.presentation.navigation.Route
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.AppTopBar
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.DrawerContent
import com.hilmyfhauzan.tokopakadam.presentation.screen.component.SideMenu
import com.hilmyfhauzan.tokopakadam.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    widthSizeClass: WindowWidthSizeClass,
    onNavigate: (Route) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var isNumpadVisible by remember { mutableStateOf(true) }

    val isTablet = widthSizeClass == WindowWidthSizeClass.Expanded

    SystemBarsVisibility(isTablet)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -10) {
                    isNumpadVisible = false
                } else if (available.y > 10) {
                    isNumpadVisible = true
                }
                return Offset.Zero
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showConfirmationDialog by retain { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiState
            .map { it.isTransactionSaved }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                // Consume event immediately so state resets
                viewModel.onTransactionSavedConsumed()
                
                // Launch independent job for snackbar so it persists even after state reset
                launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Data Transaksi Berhasil Disimpan",
                        actionLabel = "BATALKAN",
                        duration = SnackbarDuration.Long
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoLastTransaction()
                    }
                }
            }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = "Gagal: $error",
                duration = SnackbarDuration.Long
            )
        }
    }

    if (showConfirmationDialog) {
        TransactionConfirmationDialog(
            state = state,
            onDismiss = { showConfirmationDialog = false },
            onConfirm = { name, note ->
                viewModel.saveTransaction(name, note)
                showConfirmationDialog = false
            }
        )
    }

    SideMenu(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = Route.Main,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    onNavigate(route)
                }
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        modifier = Modifier.padding(12.dp),
                        action = {
                            data.visuals.actionLabel?.let { actionLabel ->
                                TextButton(
                                    onClick = { data.performAction() }
                                ) {
                                    Text(
                                        text = actionLabel,
                                        color = MaterialTheme.colorScheme.inversePrimary
                                    )
                                }
                            }
                        },
                        dismissAction = {
                            TextButton(
                                onClick = { data.dismiss() }
                            ) {
                                Text(
                                    text = "TUTUP",
                                    color = MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                        }
                    ) {
                        Text(data.visuals.message)
                    }
                }
            },
            topBar = {
                if (!isTablet) {
                    AppTopBar(
                        title = "Toko Pak Adam",
                        onDrawerClick = { scope.launch { drawerState.open() } }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier =
                    Modifier.padding(paddingValues)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .let {
                            if (!isTablet) it.nestedScroll(nestedScrollConnection)
                            else it
                        }
            ) {
                if (isTablet) {
                    // Tablet Layout (Split View)
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Left Column: Transaction List
                        Column(
                            modifier =
                                Modifier
                                    .weight(0.4f)
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ProductSelector(
                                selectedType = state.selectedProduct,
                                onProductSelected = viewModel::onProductSelected
                            )

                            TransactionSummarySection(
                                state = state,
                                onActiveInputChanged = viewModel::setActiveInput,
                                onClearAll = viewModel::clearAllTransaction
                            )
                        }

                        // Right Column: Numpad
                        Box(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                NumpadSection(
                                    onNumberClick = viewModel::onNumpadClick,
                                    onBackspaceClick = viewModel::onBackspaceClick,
                                    onClearClick = viewModel::onClearClick,
                                    onHalfTrayClick = viewModel::onHalfTrayClick,
                                    onOneTrayClick = viewModel::onOneTrayClick,
                                    isTablet = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                )

                                // Save Button for Tablet
                                Button(
                                    onClick = { 
                                         if (state.totalBelanja > 0) {
                                            showConfirmationDialog = true 
                                         }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors =
                                        ButtonDefaults.buttonColors(
                                            containerColor =
                                                MaterialTheme.colorScheme.primary
                                        ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.KeyboardReturn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ENTER",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Phone Layout (Original)
                    // Top Content
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier =
                            Modifier
                                .align(Alignment.TopCenter)
                                .verticalScroll(rememberScrollState())
                    ) {
                        // 1. Product Selector Tabs
                        ProductSelector(
                            selectedType = state.selectedProduct,
                            onProductSelected = viewModel::onProductSelected
                        )

                        TransactionSummarySection(
                            state = state,
                            onActiveInputChanged = { newInput ->
                                viewModel.setActiveInput(newInput)
                                isNumpadVisible = true
                            },
                            onClearAll = viewModel::clearAllTransaction
                        )
                        Spacer(modifier = Modifier.height(350.dp))
                    }

                    // Bottom Content (Numpad & Button)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedVisibility(
                            visible = isNumpadVisible,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it }
                        ) {
                            NumpadSection(
                                onNumberClick = viewModel::onNumpadClick,
                                onBackspaceClick = viewModel::onBackspaceClick,
                                onClearClick = viewModel::onClearClick,
                                onHalfTrayClick = viewModel::onHalfTrayClick,
                                onOneTrayClick = viewModel::onOneTrayClick,
                                isTablet = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                            )
                        }
                        
                        // 4. Enter Button (Always visible)
                        Button(
                            onClick = { 
                                if (state.totalBelanja > 0) {
                                    showConfirmationDialog = true 
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.primary
                                ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ENTER",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SystemBarsVisibility(visible: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current
    DisposableEffect(visible) {
        val window = (context as? Activity)?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)
            if (visible) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
        onDispose {
            val windowDispose = (context as? Activity)?.window
            if (windowDispose != null) {
                val insetsControllerDispose = WindowCompat.getInsetsController(windowDispose, view)
                insetsControllerDispose.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}