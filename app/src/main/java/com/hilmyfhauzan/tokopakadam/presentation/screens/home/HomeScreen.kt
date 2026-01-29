package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilmyfhauzan.tokopakadam.presentation.viewmodels.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel = koinViewModel()) {
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(containerColor = Color.White, topBar = { HomeTopBar() }) { paddingValues ->
                Column(
                        modifier =
                                Modifier.padding(paddingValues)
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        // 1. Product Selector Tabs
                        ProductSelector(
                                selectedType = state.selectedProduct,
                                onProductSelected = viewModel::onProductSelected
                        )

                        // 2. Info Cards (Input, Total, Tunai, Hutang, Kembalian)
                        TransactionSummarySection(
                                state = state,
                                onActiveInputChanged = viewModel::setActiveInput
                        )

                        // 3. Numpad Grid
                        Spacer(modifier = Modifier.weight(1f)) // Push numpad to bottom
                        NumpadSection(
                                onNumberClick = viewModel::onNumpadClick,
                                onBackspaceClick = viewModel::onBackspaceClick,
                                onClearClick = viewModel::onClearClick,
                                onHalfTrayClick = viewModel::onHalfTrayClick,
                                onOneTrayClick = viewModel::onOneTrayClick
                        )

                        // 4. Save Button
                        Button(
                                onClick = { viewModel.saveTransaction() },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors =
                                        ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                                shape = RoundedCornerShape(12.dp)
                        ) {
                                Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                        text = "SIMPAN",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                }
        }
}
