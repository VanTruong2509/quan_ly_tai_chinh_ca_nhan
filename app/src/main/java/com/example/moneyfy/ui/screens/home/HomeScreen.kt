package com.example.moneyfy.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val totalMoney by viewModel.totalMoney.collectAsState()
    val spendings = viewModel.spendings

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { HomeTopBar(navController) },
        containerColor = Color(0xFF101010)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Tổng quan tiền ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    title = "TIỀN MẶT",
                    value = "${"%,.0f".format(totalMoney)} VND",
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "Thêm giao dịch",
                    value = "+",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDialog = true }
                )
            }

            // --- Biểu đồ ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        "Chi tiêu gần đây",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    LineChart(spendings)
                }
            }

            // --- Lịch ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Column {
                    Text("Lịch", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    SimpleCalendar()
                }
            }

            // --- Thống kê ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Column {
                    Text("Thống kê chi tiêu", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    StatsSection(spendings)
                }
            }
        }

        // --- Dialog thêm chi tiêu ---
        if (showDialog) {
            AddSpendingDialog(
                onDismiss = { showDialog = false },
                onAdd = { amount ->
                    viewModel.addSpending(amount)
                    showDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Tổng quan", color = Color.White, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { navController.navigate("notification") }) {
                Icon(Icons.Default.Notifications, contentDescription = "Thông báo", tint = Color.White)
            }
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.Settings, contentDescription = "Cài đặt", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
    )
}

@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LineChart(spendings: List<Spending>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (spendings.isEmpty()) return@Canvas

        val pathColor = Color(0xFF00C853)
        val width = size.width
        val height = size.height
        val step = width / (spendings.size - 1)
        val maxAmount = (spendings.maxOfOrNull { it.amount } ?: 1f)

        // Trục ngang
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 2f
        )

        // Vẽ đường biểu đồ
        spendings.forEachIndexed { index, item ->
            if (index > 0) {
                val prev = spendings[index - 1]
                drawLine(
                    color = pathColor,
                    start = Offset((index - 1) * step, height - (prev.amount / maxAmount) * height),
                    end = Offset(index * step, height - (item.amount / maxAmount) * height),
                    strokeWidth = 5f
                )
            }
        }
    }
}

@Composable
fun SimpleCalendar() {
    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Text(day, color = Color.Gray, fontSize = 13.sp)
        }
    }
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        (1..7).forEach { day ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF2E2E2E), shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text("$day", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun StatsSection(spendings: List<Spending>) {
    val total = spendings.sumOf { it.amount.toDouble() }
    val avg = if (spendings.isNotEmpty()) total / spendings.size else 0.0

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Tổng chi tiêu: ${"%,.0f".format(total)} VND", color = Color.White, fontSize = 14.sp)
        Text("Trung bình: ${"%,.0f".format(avg)} VND/lần", color = Color.Gray, fontSize = 13.sp)
        Text("Số lần chi tiêu: ${spendings.size}", color = Color.Gray, fontSize = 13.sp)
    }
}

@Composable
fun AddSpendingDialog(onDismiss: () -> Unit, onAdd: (Float) -> Unit) {
    var input by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val amount = input.toFloatOrNull()
                if (amount != null && amount > 0) onAdd(amount)
            }) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        title = { Text("Thêm giao dịch") },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Số tiền (VND)") }
            )
        },
        containerColor = Color(0xFF1E1E1E),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}
