package com.example.moneyfy.ui.screens.invest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment               // ⭐ THÊM
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InvestmentScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                "Đầu tư",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Thông tin đầu tư sẽ xuất hiện ở đây",
            color = Color.White,
            fontSize = 18.sp
        )
    }
}
