package com.hilmyfhauzan.tokopakadam.presentation.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hilmyfhauzan.tokopakadam.presentation.state.ActiveInput
import com.hilmyfhauzan.tokopakadam.presentation.state.MainUiState
import com.hilmyfhauzan.tokopakadam.presentation.util.formatRupiah

@Composable
fun TransactionSummarySection(
    state: MainUiState,
    onActiveInputChanged: (ActiveInput) -> Unit,
    onClearAll: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val unitName = state.selectedProduct.unitName()

        // 1. Input Terkini (Active Input)
        SummaryCard(
            label = "INPUT TERKINI ($unitName)", // Tambah info satuan di label
            value = "${state.getCurrentQtyDisplay()} $unitName", // Contoh: "15 Butir"
            valueColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor =
                if (state.activeInput == ActiveInput.QUANTITY)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.background,
            borderColor =
                if (state.activeInput == ActiveInput.QUANTITY)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
            onClick = { onActiveInputChanged(ActiveInput.QUANTITY) }
        )

        // List Detail Transaksi (Item yang sudah diinput)
        val transactionItems = state.quantities.filter { (_, qty) -> qty > 0 }

        if (transactionItems.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Header: Label
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detail Transaksi",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                transactionItems.forEach { (type, qty) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        Text(
                            text = type.displayName,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${qty.toInt()} ${type.unitName()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Delete All Button
                OutlinedButton(
                    onClick = onClearAll,
                    modifier =
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(40.dp),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    contentPadding = PaddingValues(0.dp)
                ) { Text(text = "Hapus Semua", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) }
            }
        }

        // 2. Total Belanja
        SummaryCard(
            label = "TOTAL BELANJA",
            value = formatRupiah(state.totalBelanja),
            valueColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.background,
            isLeadingIcon = true // Simulating the orange bar on left
        )

        // 3. Tunai & Hutang
        SummaryCard(
            label = "TUNAI",
            value = if (state.cashInput == 0L) "" else formatRupiah(state.cashInput),
            valueColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor =
                if (state.activeInput == ActiveInput.CASH)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface,
            borderColor =
                if (state.activeInput == ActiveInput.CASH) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
            onClick = { onActiveInputChanged(ActiveInput.CASH) },
            showPlaceholder = state.cashInput == 0L
        )

        SummaryCard(
            label = "HUTANG",
            value = formatRupiah(state.hutang),
            valueColor = MaterialTheme.colorScheme.error,
            backgroundColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outlineVariant
        )

        // 4. Kembalian
        SummaryCard(
            label = "KEMBALIAN",
            value = formatRupiah(state.kembali),
            valueColor = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}

@Composable
fun SummaryCard(
    label: String,
    value: String,
    valueColor: Color,
    backgroundColor: Color,
    borderColor: Color? = null,
    isLeadingIcon: Boolean = false,
    onClick: (() -> Unit)? = null,
    showPlaceholder: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
		border = borderColor?.let { BorderStroke(1.dp, it) },
		modifier =
			Modifier.fillMaxWidth().height(50.dp).let {
				if (onClick != null) it.clickable { onClick() } else it
			} // Compact height
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            if (isLeadingIcon) {
                Box(
                    modifier =
                        Modifier.fillMaxHeight()
                            .width(4.dp)
                            .background(MaterialTheme.colorScheme.primary)
                )
            }

            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showPlaceholder) {
                    Text(
                        text = "0",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = value,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = valueColor
                    )
                }
            }
        }
    }
}
