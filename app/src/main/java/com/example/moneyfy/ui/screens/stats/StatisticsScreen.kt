package com.example.moneyfy.ui.screens.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatisticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        Text("Thống kê", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
                .padding(12.dp)
        ) {
            BarChart()
        }
    }
}

@Composable
fun BarChart() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / 7
        val maxHeight = size.height

        val data = listOf(30, 50, 20, 80, 60, 40, 70)
        val maxValue = data.maxOrNull() ?: 1

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue.toFloat()) * maxHeight
            drawRect(
                color = Color(0xFF00C853),
                topLeft = Offset(index * barWidth + 10f, maxHeight - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth - 20f, barHeight)
            )
        }
    }
}
