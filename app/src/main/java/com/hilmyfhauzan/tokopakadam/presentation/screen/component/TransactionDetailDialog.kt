package com.hilmyfhauzan.tokopakadam.presentation.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryTransaction
import com.hilmyfhauzan.tokopakadam.domain.model.TransactionStatus
import com.hilmyfhauzan.tokopakadam.presentation.util.CurrencyVisualTransformation
import com.hilmyfhauzan.tokopakadam.presentation.util.formatRupiah

@Composable
fun TransactionDetailDialog(
    transaction: HistoryTransaction,
    onDismiss: () -> Unit,
    onSave: (String, Long, String) -> Unit
) {
    var customerName by remember { mutableStateOf(transaction.customerName ?: "") }
    var status by remember { mutableStateOf(transaction.status) }
    var paymentMethod by remember { mutableStateOf(transaction.paymentMethod) }
    var cashStr by remember { mutableStateOf(transaction.cash.toString()) }
    var note by remember { mutableStateOf(transaction.note ?: "") }

    var isStatusExpanded by remember { mutableStateOf(false) }
    var isPaymentExpanded by remember { mutableStateOf(false) }

    val amount = transaction.amount
    val cash = cashStr.toLongOrNull() ?: 0L

    val remainingDebt by remember(amount, cash) {
        derivedStateOf {
            if (cash < amount) amount - cash else 0L
        }
    }

    val change by remember(amount, cash) {
        derivedStateOf {
            if (cash > amount) cash - amount else 0L
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Transaksi") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Read Only Fields
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailText(label = "ID Transaksi", value = transaction.id)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailText(
                            label = "Tanggal",
                            value = transaction.date,
                            modifier = Modifier.weight(1f)
                        )
                        DetailText(
                            label = "Waktu",
                            value = transaction.time,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailText(
                            label = "Items",
                            value = transaction.items,
                            modifier = Modifier.weight(1f)
                        )

                        DetailText(
                            label = "Total Belanja",
                            value = formatRupiah(transaction.amount),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Editable Fields
                Text(
                    text = "Edit Data",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Nama Pelanggan") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Status Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = status.label,
                        onValueChange = {},
                        label = { Text("Status") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector =
                                    if(isStatusExpanded) Icons.Default.ArrowDropUp
                                    else Icons.Default.ArrowDropDown,
                                contentDescription = "Drop"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                            .clickable { isStatusExpanded = true }
                    )
                    // Overlay transparent button to capture click if readOnly textfield doesn't work well with click
                    Box(modifier = Modifier
                        .matchParentSize()
                        .clickable { isStatusExpanded = true })

                    DropdownMenu(
                        expanded = isStatusExpanded,
                        onDismissRequest = { isStatusExpanded = false },
                        containerColor = MaterialTheme.colorScheme.surface                        
                    ) {
                        TransactionStatus.entries.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.label) },
                                onClick = {
                                    status = item
                                    isStatusExpanded = false
                                }
                            )
                        }
                    }
                }

                // Payment Method Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = paymentMethod,
                        onValueChange = {},
                        label = { Text("Metode Pembayaran") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector =
                                    if(isPaymentExpanded) Icons.Default.ArrowDropUp
                                    else Icons.Default.ArrowDropDown,
                                contentDescription = "Drop"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(modifier = Modifier
                        .matchParentSize()
                        .clickable { isPaymentExpanded = true })

                    DropdownMenu(
                        expanded = isPaymentExpanded,
                        onDismissRequest = { isPaymentExpanded = false },
                        containerColor = MaterialTheme.colorScheme.surface 
                    ) {
                        listOf("Tunai", "Transfer Bank").forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    paymentMethod = item
                                    isPaymentExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = cashStr,
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }
                        cashStr = digits
                    },
                    label = { Text("Tunai (Cash)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = remember { CurrencyVisualTransformation() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Catatan") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Calculated Results
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailText(
                        label = "Sisa Hutang",
                        value = formatRupiah(remainingDebt),
                        modifier = Modifier.weight(1f),
                        valueColor =
                            if(remainingDebt > 0) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface
                    )
                    DetailText(
                        label = "Kembalian",
                        value = formatRupiah(change),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(customerName, cash, note)
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
private fun DetailText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}