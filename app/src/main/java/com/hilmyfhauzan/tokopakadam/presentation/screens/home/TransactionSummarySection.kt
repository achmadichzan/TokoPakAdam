package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hilmyfhauzan.tokopakadam.presentation.state.MainUiState
import com.hilmyfhauzan.tokopakadam.presentation.util.formatRupiah

@Composable
fun TransactionSummarySection(state: MainUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        val unitName = state.selectedProduct.defaultUnit()

        // 1. Input Terkini (Active Input)
        SummaryCard(
            label = "INPUT TERKINI ($unitName)", // Tambah info satuan di label
            value = "${state.getCurrentQtyDisplay()} $unitName", // Contoh: "15 Butir"
            valueColor = TextBlack,
            backgroundColor = BackgroundGray
        )

        // 2. Total Belanja
        SummaryCard(
            label = "TOTAL BELANJA",
            value = formatRupiah(state.totalBelanja),
            valueColor = PrimaryOrange,
            backgroundColor = BackgroundGray,
            isLeadingIcon = true // Simulating the orange bar on left
        )

        // 3. Tunai & Hutang
        SummaryCard(
            label = "TUNAI",
            value = formatRupiah(state.cashInput),
            valueColor = TextBlack,
            backgroundColor = Color.White,
            borderColor = Color.LightGray
        )

        SummaryCard(
            label = "HUTANG",
            value = formatRupiah(state.hutang),
            valueColor = DangerRed,
            backgroundColor = Color.White,
            borderColor = Color.LightGray
        )

        // 4. Kembalian
        SummaryCard(
            label = "KEMBALIAN",
            value = formatRupiah(state.kembali),
            valueColor = SuccessGreen,
            backgroundColor = SuccessGreenBg
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
    isLeadingIcon: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = borderColor?.let { BorderStroke(1.dp, it) },
        modifier = Modifier.fillMaxWidth().height(50.dp) // Compact height
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLeadingIcon) {
                Box(modifier = Modifier.fillMaxHeight().width(4.dp).background(PrimaryOrange))
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextGray)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = valueColor)
            }
        }
    }
}