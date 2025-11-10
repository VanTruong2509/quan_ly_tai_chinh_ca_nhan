package com.example.moneyfy.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun ExpenseScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Chi tiêu",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF101010)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF101010))
        ) {
            // Thêm Scrollable nội dung
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    "Danh mục & Nhãn",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Text("Tiền của tôi đi đâu?", color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(16.dp))

                CategoryRow("Danh mục", "Nhận")
                Spacer(Modifier.height(16.dp))
                ExpenseChart()
                Spacer(Modifier.height(16.dp))

                // --- Nút "Hiển thị ở bản ghi" ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF007AFF), shape = MaterialTheme.shapes.medium)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Hiển thị ở bản ghi",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(80.dp))
            }

            // --- Nút chọn tháng (Cố định ở dưới) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E))
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("<", color = Color.White, fontSize = 18.sp)
                Text(
                    "Tháng này ▼",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(">", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun CategoryRow(left: String, right: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(left, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(right, color = Color(0xFF007AFF), fontSize = 15.sp)
    }
}

@Composable
fun ExpenseChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Không có dữ liệu trong giai đoạn này",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}
