package com.example.moneyfy.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moneyfy.data.model.Spending
import com.example.moneyfy.ui.screens.home.HomeViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun StatsScreen(navController: NavController, viewModel: HomeViewModel) {

    // Điều khiển chuyển giữa 2 màn hình
    var showExpenseList by remember { mutableStateOf(false) }

    val spendings by viewModel.spendings.collectAsState()
    val totalMoney by viewModel.totalMoney.collectAsState()

    if (!showExpenseList) {
        // ==================== MÀN HÌNH CHÍNH ====================
        StatsMainScreen(
            spendings = spendings,
            totalMoney = totalMoney,
            onExpenseClick = { showExpenseList = true } // Bấm để mở danh sách chi
        )
    } else {
        // ==================== MÀN HÌNH DANH SÁCH CHI ====================
        ExpenseListScreen(
            spendings = spendings,
            onBack = { showExpenseList = false }
        )
    }
}

/* ----------------------------------------------------------
    MÀN HÌNH 1: THỐNG KÊ
---------------------------------------------------------- */
@Composable
fun StatsMainScreen(
    spendings: List<Spending>,
    totalMoney: Float,
    onExpenseClick: () -> Unit
) {
    val totalSpent = spendings.filter { it.amount > 0f }.sumOf { it.amount.toDouble() }
    val totalIncome = spendings.filter { it.amount < 0f }.sumOf { kotlin.math.abs(it.amount.toDouble()) }

    val formatter = NumberFormat.getNumberInstance(Locale.US)

    val totalSpentDisplay = if (totalSpent <= 0) "0 đ" else "${formatter.format(totalSpent.toInt())} đ"
    val totalIncomeDisplay = if (totalIncome <= 0) "0 đ" else "${formatter.format(totalIncome.toInt())} đ"
    val balanceDisplay = if (totalMoney <= 0) "0 đ" else "${formatter.format(totalMoney.toInt())} đ"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        Text("Thống Kê", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        StatRow("SỐ DƯ", balanceDisplay, Color(0xFF00C853))
        StatRow("CHI TIÊU", totalSpentDisplay, Color(0xFFFF5252)) {
            onExpenseClick()
        }
        StatRow("THU NHẬP", totalIncomeDisplay, Color(0xFF448AFF))
    }
}

/* ----------------------------------------------------------
    ROW ITEM
---------------------------------------------------------- */
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

/* ----------------------------------------------------------
    MÀN HÌNH 2: DANH SÁCH CHI
---------------------------------------------------------- */
@Composable
fun ExpenseListScreen(
    spendings: List<Spending>,
    onBack: () -> Unit
) {
    val expenseList = spendings.filter { it.amount > 0f }

    val timeFormatter = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        // Nút quay lại
        Text(
            text = "← Quay lại",
            color = Color.Cyan,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onBack() }
        )
        Spacer(Modifier.height(16.dp))

        Text(
            "Danh sách chi tiêu",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(expenseList) { item ->
                ExpenseItem(
                    category = item.category,
                    amount = item.amount,
                    note = item.note,
                    time = timeFormatter.format(item.timestamp)
                )
            }
        }
    }
}

/* ----------------------------------------------------------
    ITEM HIỂN THỊ GIAO DỊCH
---------------------------------------------------------- */
@Composable
fun ExpenseItem(category: String, amount: Float, note: String, time: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFF1E1E1E), MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(category, color = Color.White, fontWeight = FontWeight.Bold)
        Text("${amount.toInt()} đ", color = Color.Red)
        if (note.isNotEmpty()) Text("Ghi chú: $note", color = Color.Gray, fontSize = 13.sp)
        Text(time, color = Color.Gray, fontSize = 12.sp)
    }
}
