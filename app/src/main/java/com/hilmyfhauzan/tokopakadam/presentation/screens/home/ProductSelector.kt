package com.hilmyfhauzan.tokopakadam.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.hilmyfhauzan.tokopakadam.domain.model.ProductType

@Composable
fun ProductSelector(
    selectedType: ProductType,
    onProductSelected: (ProductType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductType.entries.forEach { type ->
            val isSelected = selectedType == type
            val activeColor = if (type == ProductType.TOFU) SecondaryBlue else PrimaryOrange

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .clickable { onProductSelected(type) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = if (isSelected) BorderStroke(2.dp, activeColor) else BorderStroke(1.dp, Color.LightGray),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Colored Strip on Left
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(6.dp)
                                .background(activeColor)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Split name "Telur Kecil" -> "Telur" & "Kecil"
                        val words = type.displayName.split(" ")
                        if (words.size > 1) {
                            Text(text = words[0], fontSize = 10.sp, color = TextGray)
                            Text(text = words[1], fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                        } else {
                            Text(text = "LAINNYA", fontSize = 10.sp, color = TextGray)
                            Text(text = words[0], fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                        }
                    }
                }
            }
        }
    }
}