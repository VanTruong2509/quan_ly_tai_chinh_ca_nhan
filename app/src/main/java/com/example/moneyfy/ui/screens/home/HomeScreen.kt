package com.example.moneyfy.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import com.example.moneyfy.data.model.Spending
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val totalMoney by viewModel.totalMoney.collectAsState()
    val spendings by viewModel.spendings.collectAsState()
    val weeklySpending by remember { derivedStateOf { viewModel.getWeeklySpending() } }

    Scaffold(
        topBar = { HomeTopBar(navController) },
        containerColor = Color(0xFF101010)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        title = "TIỀN MẶT",
                        value = "${NumberFormat.getNumberInstance().format(totalMoney.toInt())} VND",
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        title = "Thêm tài khoản",
                        value = "+",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { navController.navigate("create_account") }
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            "Chi tiêu tuần",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        LineChart(weeklySpending)
                    }
                }
            }

            item {
                Text(
                    "Giao dịch gần đây",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(spendings.reversed()) { spending ->
                SpendingItem(spending)
            }
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
    if (spendings.isEmpty()) return

    val days = listOf("T2","T3","T4","T5","T6","T7","CN")
    val data = spendings.map { it.amount }
    val maxAmount = data.maxOrNull()?.let { if(it<=0)1f else it } ?: 1f

    Column(modifier = Modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFF1E1E1E))
        ) {
            val width = size.width
            val height = size.height
            val step = if(data.size>1) width/(data.size-1) else width

            drawLine(
                color = Color.Gray.copy(alpha=0.3f),
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = 2f
            )

            data.forEachIndexed { index, amount ->
                if(index>0){
                    val prev = data[index-1]
                    drawLine(
                        color = Color(0xFF00C853),
                        start = Offset((index-1)*step, height-(prev/maxAmount)*height),
                        end = Offset(index*step, height-(amount/maxAmount)*height),
                        strokeWidth = 3f
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Text(day, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SpendingItem(spending: Spending) {
    val isExpense = spending.amount >= 0
    val displayAmount = kotlin.math.abs(spending.amount)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "${NumberFormat.getNumberInstance(Locale.US).format(displayAmount.toInt())} VND",
                color = if(isExpense) Color.Red else Color.Green,
                fontWeight = FontWeight.Bold
            )
            Text("Danh mục: ${spending.category}", color = Color.Gray, fontSize = 13.sp)
            if(spending.note.isNotEmpty()){
                Text("Ghi chú: ${spending.note}", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}
