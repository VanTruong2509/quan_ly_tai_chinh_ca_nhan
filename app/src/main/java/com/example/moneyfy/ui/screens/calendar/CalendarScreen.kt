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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
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

    // --- TRẠNG THÁI THÁNG VÀ NĂM ĐANG HIỂN THỊ ---
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val realMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    val realYear = Calendar.getInstance().get(Calendar.YEAR)

    // Calendar xử lý tháng/năm
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.MONTH, currentMonth - 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val startDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 6) % 7

    // Tạo danh sách ngày
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

        // --- TIÊU ĐỀ THÁNG + NÚT LÙI / TIẾN ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {
                currentMonth--
                if (currentMonth < 1) {
                    currentMonth = 12
                    currentYear--
                }
                selectedDay = null
            }) {
                Icon(Icons.Default.ArrowBackIos, contentDescription = "Previous", tint = Color.White)
            }

            Text(
                "Tháng $currentMonth, $currentYear",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = {
                currentMonth++
                if (currentMonth > 12) {
                    currentMonth = 1
                    currentYear++
                }
                selectedDay = null
            }) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Next", tint = Color.White)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Tên các ngày trong tuần
        val weekdays = listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            weekdays.forEach {
                Text(it, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(8.dp))

        // --- Lưới ngày trong tháng ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { dayStr ->
                val dayInt = dayStr.toIntOrNull()

                val isToday =
                    dayInt != null && dayInt == today && currentMonth == realMonth && currentYear == realYear

                val bgColor = when {
                    dayInt == selectedDay -> Color(0xFF2979FF)
                    isToday -> Color(0xFF00C853)
                    else -> Color(0xFF1E1E1E)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(bgColor, shape = MaterialTheme.shapes.small)
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
                        .clickable(enabled = dayInt != null && dailySpending.containsKey(dayInt)) {
                            selectedDay = if (selectedDay == dayInt) null else dayInt
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(dayStr, color = Color.White)

                        if (dayInt != null && dailySpending[dayInt] != null) {
                            Text(
                                text = "${dailySpending[dayInt]!!.toInt()}đ",
                                color = Color.Red,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- DANH SÁCH GIAO DỊCH CỦA NGÀY ĐƯỢC CHỌN ---
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
                    LazyColumn(modifier = Modifier.height(250.dp)) {
                        items(spendingsOfDay.size) { index ->
                            val s = spendingsOfDay[index]

                            val time = SimpleDateFormat("HH:mm", Locale.getDefault())
                                .format(Date(s.timestamp))

                            Text(
                                "$time - ${s.category}: ${
                                    NumberFormat.getNumberInstance(Locale.US)
                                        .format(s.amount.toInt())
                                } đ" +
                                        if (s.note.isNotEmpty()) " - ${s.note}" else "",
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
