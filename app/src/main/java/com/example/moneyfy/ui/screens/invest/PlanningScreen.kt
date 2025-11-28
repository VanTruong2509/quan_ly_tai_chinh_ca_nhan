package com.example.moneyfy.ui.screens.invest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlanningScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        // --- HEADER ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }

            Text(
                text = "Lập kế hoạch",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // --- CARD ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A74C4))
                .padding(16.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text("Ngân sách", color = Color.White, fontSize = 18.sp)
                Text("Kế hoạch chi tiêu của bạn", color = Color.White, fontSize = 14.sp)
            }

            Icon(
                imageVector = Icons.Default.ArrowBack,   // thay icon của bạn
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
