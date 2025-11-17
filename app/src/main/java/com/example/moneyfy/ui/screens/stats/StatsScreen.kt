package com.example.moneyfy.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun StatsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        Text(
            "Thống Kê",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        // Khi bấm vào “SỐ DƯ” -> sang BalanceScreen
        StatRow(
            title = "SỐ DƯ",
            value = "0,00 đ",
            color = Color(0xFF00C853)
        ) {
            navController.navigate("balance")
        }

        // Khi bấm vào “CHI TIÊU” -> sang ExpenseScreen
        StatRow(
            title = "CHI TIÊU",
            value = "0,00 đ",
            color = Color(0xFFFF5252)
        ) {
            navController.navigate("expense")
        }

        // “THU NHẬP” tạm thời chưa điều hướng
        StatRow(
            title = "THU NHẬP",
            value = "0,00 đ",
            color = Color(0xFF448AFF)
        )
    }
}

@Composable
fun StatRow(
    title: String,
    value: String,
    color: Color,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
            .clickable(enabled = onClick != null) { onClick?.invoke() } // Thêm sự kiện bấm
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        Text(value, color = color, fontWeight = FontWeight.Bold)
    }
}
