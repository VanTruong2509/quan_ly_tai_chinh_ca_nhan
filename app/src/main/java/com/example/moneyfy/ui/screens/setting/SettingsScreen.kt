package com.example.moneyfy.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit, // callback khi bấm nút quay lại
    onItemClick: (String) -> Unit
) {
    val buttonList = listOf(
        Triple("Thông tin cá nhân", Icons.Default.Person, Color(0xFF1565C0)),
        Triple("Đổi mật khẩu", Icons.Default.Lock, Color(0xFF1565C0)),
        Triple("Ngôn ngữ", Icons.Default.Language, Color(0xFF1565C0)),
        Triple("Đơn vị tiền tệ", Icons.Default.AttachMoney, Color(0xFF1565C0)),
        Triple("Các tài khoản", Icons.Default.AccountBox, Color(0xFF1565C0)),
        Triple("Báo lỗi hoặc phản hồi", Icons.Default.Chat, Color(0xFF1565C0)),
        Triple("Đánh giá", Icons.Default.Favorite, Color(0xFF1565C0)),
        Triple("Đăng xuất", Icons.Default.PowerSettingsNew, Color(0xFFD32F2F))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 🔙 Thanh tiêu đề có nút quay lại
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cài đặt",
                color = Color.White,
                fontSize = 22.sp,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Các nút chức năng
        buttonList.forEach { (text, icon, color) ->
            Button(
                onClick = { onItemClick(text) },
                colors = ButtonDefaults.buttonColors(containerColor = color),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = text,
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = text, color = Color.White)
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = "Mở",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
