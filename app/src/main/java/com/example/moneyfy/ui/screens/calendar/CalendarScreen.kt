package com.example.moneyfy.ui.screens.calendar

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarScreen() {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1 // 0-indexed
    val year = calendar.get(Calendar.YEAR)

    // Lấy số ngày trong tháng
    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Ngày bắt đầu tháng (0=Chủ nhật, 1=Thứ 2...)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 6) % 7 // chuyển 1-7 → 0-6

    val days = mutableListOf<String>()
    repeat(startDayOfWeek) { days.add("") } // thêm ô trống đầu tháng
    for (day in 1..maxDay) days.add(day.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        // Tiêu đề tháng
        Text(
            text = "Tháng $month, $year",
            color = Color.White,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(12.dp))

        // Tên các ngày trong tuần
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val weekdays = listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
            weekdays.forEach { day ->
                Text(day, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f), maxLines = 1)
            }
        }
        Spacer(Modifier.height(8.dp))

        // Lưới ngày
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(days) { day ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                if (day.toIntOrNull() == today) Color(0xFF00C853)
                                else Color(0xFF1E1E1E),
                                shape = MaterialTheme.shapes.small
                            )
                            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            color = if (day.toIntOrNull() == today) Color.White else Color.Gray
                        )
                    }
                }
            }
        )
    }
}
