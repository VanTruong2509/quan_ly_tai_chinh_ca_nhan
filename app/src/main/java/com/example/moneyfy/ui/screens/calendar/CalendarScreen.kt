package com.example.moneyfy.ui.screens.calendar

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyfy.data.model.Spending
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(spendings: List<Spending>) {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentYear = calendar.get(Calendar.YEAR)

    // Số ngày trong tháng
    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 6) % 7

    // Danh sách ngày có chừa ô trống
    val days = mutableListOf<String>()
    repeat(startDayOfWeek) { days.add("") }
    for (day in 1..maxDay) days.add(day.toString())

    // Tổng chi theo ngày
    val dailySpending: Map<Int, Float> = spendings
        .filter { s ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = s.timestamp
            cal.get(Calendar.MONTH) + 1 == currentMonth &&
                    cal.get(Calendar.YEAR) == currentYear
        }
        .groupBy { s ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = s.timestamp
            cal.get(Calendar.DAY_OF_MONTH)
        }
        .mapValues { entry -> entry.value.sumOf { it.amount.toDouble() }.toFloat() }

    var selectedDay by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        // Tiêu đề tháng
        Text("Tháng $currentMonth, $currentYear", color = Color.White, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))

        // Tên các ngày trong tuần
        val weekdays = listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            weekdays.forEach {
                Text(it, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f), maxLines = 1)
            }
        }
        Spacer(Modifier.height(8.dp))

        // Lưới ngày
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { dayStr ->
                val dayInt = dayStr.toIntOrNull()
                val isFuture = dayInt != null && dayInt > today

                val bgColor = when {
                    dayInt == selectedDay -> Color(0xFF2979FF)
                    dayInt == today -> Color(0xFF00C853)
                    isFuture -> Color(0xFF555555)
                    else -> Color(0xFF1E1E1E)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(bgColor, shape = MaterialTheme.shapes.small)
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
                        .clickable(
                            enabled = dayInt != null && !isFuture && dailySpending.containsKey(dayInt)
                        ) {
                            selectedDay = if (selectedDay == dayInt) null else dayInt
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(dayStr, color = Color.White)
                        if (dayInt != null && dailySpending[dayInt] != null) {
                            Text(
                                text = "${dailySpending[dayInt]!!.toInt()} đ",
                                color = Color.Red,
                                fontSize = 10.sp
                            )
                        }
                        if (isFuture) {
                            Text("—", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // KHU VỰC DANH SÁCH GIAO DỊCH — DÙNG LAZYCOLUMN
        selectedDay?.let { day ->
            val spendingsOfDay = spendings.filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.timestamp
                cal.get(Calendar.DAY_OF_MONTH) == day &&
                        cal.get(Calendar.MONTH) + 1 == currentMonth &&
                        cal.get(Calendar.YEAR) == currentYear
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Text(
                    "Chi tiêu ngày $day",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                if (spendingsOfDay.isEmpty()) {
                    Text("Không có chi tiêu", color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)    // cho phép kéo
                    ) {
                        items(spendingsOfDay.size) { index ->
                            val s = spendingsOfDay[index]

                            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val time = timeFormat.format(Date(s.timestamp))

                            Text(
                                "${time} - ${s.category}: ${
                                    NumberFormat.getNumberInstance(Locale.US)
                                        .format(s.amount.toInt())
                                } đ ${if (s.note.isNotEmpty()) "- ${s.note}" else ""}",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
