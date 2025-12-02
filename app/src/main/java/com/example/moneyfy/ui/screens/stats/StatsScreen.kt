package com.example.moneyfy.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moneyfy.ui.screens.home.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatsScreen(navController: NavController, viewModel: HomeViewModel) {
    val spendings by viewModel.spendings.collectAsState()
    val totalMoney by viewModel.totalMoney.collectAsState()

    // Tính tổng chi và tổng thu nhập
    val totalSpent = spendings.filter { it.amount > 0f }.sumOf { it.amount.toDouble() }
    val totalIncome = spendings.filter { it.amount < 0f }.sumOf { kotlin.math.abs(it.amount.toDouble()) }

    // Format hiển thị
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    val totalSpentDisplay = if (totalSpent <= 0) "0 đ" else "${formatter.format(totalSpent.toInt())} đ"
    val totalIncomeDisplay = if (totalIncome <= 0) "0 đ" else "${formatter.format(totalIncome.toInt())} đ"
    val balanceDisplay = if (totalMoney <= 0f) "0 đ" else "${formatter.format(totalMoney.toInt())} đ"

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

        StatRow(
            title = "SỐ DƯ",
            value = balanceDisplay,
            color = Color(0xFF00C853)
        ) {
            navController.navigate("balance")
        }

        StatRow(
            title = "CHI TIÊU",
            value = totalSpentDisplay,
            color = Color(0xFFFF5252)
        ) {
            navController.navigate("expense")
        }

        StatRow(
            title = "THU NHẬP",
            value = totalIncomeDisplay,
            color = Color(0xFF448AFF)
        ) // Tạm thời chưa điều hướng
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
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        Text(value, color = color, fontWeight = FontWeight.Bold)
    }
}
