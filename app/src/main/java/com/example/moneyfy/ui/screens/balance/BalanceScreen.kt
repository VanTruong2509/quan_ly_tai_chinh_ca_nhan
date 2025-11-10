package com.example.moneyfy.ui.screens.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Số dư", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFF101010)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Các tài khoản",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                BalanceCard(title = "Tiền mặt", value = "0,00 đ")
            }

            // --- Nút chọn tháng ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007AFF), shape = MaterialTheme.shapes.medium)
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("<", color = Color.White, fontSize = 16.sp)
                Text("Tháng này ▼", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(">", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun BalanceCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(title, color = Color.White, fontSize = 15.sp)
        Spacer(Modifier.height(8.dp))
        Text(value, color = Color(0xFF00C853), fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
