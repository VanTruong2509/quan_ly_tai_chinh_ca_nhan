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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val spendings by viewModel.spendings.collectAsState()

    // ✅ TUẦN HIỆN TẠI / TUẦN TRƯỚC / TUẦN SAU
    var weekOffset by remember { mutableStateOf(0) }

    // ✅ BIỂU ĐỒ THEO TUẦN
    val weeklySpending by remember {
        derivedStateOf {
            groupSpendingByWeek(spendings, weekOffset)
        }
    }

    // ✅ LỌC GIAO DỊCH THEO TUẦN ĐANG XEM
    val filteredSpendings by remember {
        derivedStateOf {
            filterSpendingsByWeek(spendings, weekOffset)
        }
    }

    val totalSpent = spendings.filter { it.amount > 0f }.sumOf { it.amount.toDouble() }
    val spentDisplay = when {
        totalSpent <= 0.0 -> "0đ"
        totalSpent < 1000 -> "${totalSpent.toInt()} đ"
        else -> "${NumberFormat.getNumberInstance().format(totalSpent.toInt())} đ"
    }

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
                        title = "TỔNG CHI",
                        value = spentDisplay,
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
                        .height(240.dp)
                        .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                        .padding(12.dp)
                ) {
                    Column {

                        // ✅ THANH ĐIỀU HƯỚNG TUẦN + NGÀY THÁNG
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { weekOffset-- }) {
                                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                            }

                            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                Text(
                                    text = getWeekTitle(weekOffset),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = getWeekDateRange(weekOffset),
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(onClick = { weekOffset++ }) {
                                Icon(Icons.Default.ArrowForward, null, tint = Color.White)
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        BarChart(weeklySpending)
                    }
                }
            }

            item {
                Text(
                    "Giao dịch trong tuần",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // ✅ CHỈ HIỂN THỊ GIAO DỊCH ĐÚNG TUẦN
            items(filteredSpendings.reversed()) { spending ->
                SpendingItem(spending)
            }
        }
    }
}

/* ================= GIỮ NGUYÊN TOÀN BỘ PHẦN DƯỚI ================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Tổng quan", color = Color.White, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { navController.navigate("notification") }) {
                Icon(Icons.Default.Notifications, null, tint = Color.White)
            }
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.Settings, null, tint = Color.White)
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
fun BarChart(data: List<Float>) {
    if (data.isEmpty()) return

    val days = listOf("T2","T3","T4","T5","T6","T7","CN")
    val maxAmount = data.maxOrNull()?.let { if (it <= 0f) 1f else it } ?: 1f

    Column(modifier = Modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFF1E1E1E))
        ) {
            val width = size.width
            val height = size.height
            val barWidth = width / (data.size * 2f)

            val textPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 28f
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }

            data.forEachIndexed { index, amount ->
                val barHeight = (amount / maxAmount) * height
                val x = index * (barWidth * 2) + barWidth
                val y = height - barHeight

                drawRect(
                    color = Color(0xFF00C853),
                    topLeft = Offset(x, y),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                )

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        amount.toInt().toString(),
                        x + barWidth / 2,
                        y - 8,
                        textPaint
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach {
                Text(it, color = Color.Gray, fontSize = 12.sp)
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
                color = if (isExpense) Color.Red else Color.Green,
                fontWeight = FontWeight.Bold
            )
            Text("Danh mục: ${spending.category}", color = Color.Gray, fontSize = 13.sp)
            if (spending.note.isNotEmpty()) {
                Text("Ghi chú: ${spending.note}", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

/* ✅ GỘP TIỀN THEO TUẦN */
fun groupSpendingByWeek(
    spendings: List<Spending>,
    weekOffset: Int
): List<Float> {

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startOfWeek = calendar.timeInMillis

    calendar.add(Calendar.DAY_OF_WEEK, 6)
    val endOfWeek = calendar.timeInMillis

    val dailyTotals = MutableList(7) { 0f }

    spendings.forEach { spending ->
        if (spending.timestamp in startOfWeek..endOfWeek) {
            val cal = Calendar.getInstance().apply {
                timeInMillis = spending.timestamp
            }

            val dayIndex = when (cal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                Calendar.SUNDAY -> 6
                else -> 0
            }

            if (spending.amount > 0f) {
                dailyTotals[dayIndex] += spending.amount
            }
        }
    }

    return dailyTotals
}

/* ✅ LỌC GIAO DỊCH THEO TUẦN */
fun filterSpendingsByWeek(
    spendings: List<Spending>,
    weekOffset: Int
): List<Spending> {

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startOfWeek = calendar.timeInMillis

    calendar.add(Calendar.DAY_OF_WEEK, 6)
    val endOfWeek = calendar.timeInMillis

    return spendings.filter {
        it.timestamp in startOfWeek..endOfWeek
    }
}

fun getWeekTitle(weekOffset: Int): String {
    return when {
        weekOffset == 0 -> "Tuần này"
        weekOffset < 0 -> "Tuần trước"
        else -> "Tuần sau"
    }
}

fun getWeekDateRange(weekOffset: Int): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.WEEK_OF_YEAR, weekOffset)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    val startDay = cal.get(Calendar.DAY_OF_MONTH)
    val startMonth = cal.get(Calendar.MONTH) + 1

    cal.add(Calendar.DAY_OF_WEEK, 6)

    val endDay = cal.get(Calendar.DAY_OF_MONTH)
    val endMonth = cal.get(Calendar.MONTH) + 1
    val year = cal.get(Calendar.YEAR)

    return "%02d/%02d - %02d/%02d/%d".format(
        startDay, startMonth,
        endDay, endMonth,
        year
    )
}
