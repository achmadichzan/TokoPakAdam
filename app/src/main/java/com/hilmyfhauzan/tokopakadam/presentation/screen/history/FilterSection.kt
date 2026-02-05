package com.hilmyfhauzan.tokopakadam.presentation.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hilmyfhauzan.tokopakadam.domain.model.HistoryFilter

@Composable
fun FilterSection(
    currentFilter: HistoryFilter,
    onFilterSelected: (HistoryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipItem(
            text = "Hari Ini",
            selected = currentFilter == HistoryFilter.TODAY,
            onClick = { onFilterSelected(HistoryFilter.TODAY) }
        )
        FilterChipItem(
            text = "Kemarin",
            selected = currentFilter == HistoryFilter.YESTERDAY,
            onClick = { onFilterSelected(HistoryFilter.YESTERDAY) }
        )
        FilterChipItem(
            text = "7 Hari Terakhir",
            selected = currentFilter == HistoryFilter.LAST_7_DAYS,
            onClick = { onFilterSelected(HistoryFilter.LAST_7_DAYS) }
        )
        var sortMenuExpanded by remember { mutableStateOf(false) }
        Box {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color =
                    if (currentFilter == HistoryFilter.ALL ||
                        currentFilter == HistoryFilter.LAST_30_DAYS)
                        MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.surface,
                modifier = Modifier.height(32.dp).width(48.dp),
                onClick = { sortMenuExpanded = true }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Sort,
                        contentDescription = "Filter",
                        tint =
                            if (currentFilter == HistoryFilter.ALL ||
                                currentFilter == HistoryFilter.LAST_30_DAYS)
                                MaterialTheme.colorScheme.onSecondary
                            else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                DropdownMenuItem(
                    text = { Text("Semua") },
                    onClick = {
                        onFilterSelected(HistoryFilter.ALL)
                        sortMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("30 Hari Terakhir") },
                    onClick = {
                        onFilterSelected(HistoryFilter.LAST_30_DAYS)
                        sortMenuExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChipItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color =
            if (selected) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.surface,
        modifier = Modifier.height(32.dp),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color =
                        if (selected) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}