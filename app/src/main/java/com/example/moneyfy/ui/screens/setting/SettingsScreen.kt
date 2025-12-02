package com.example.moneyfy.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyfy.data.model.User
import com.example.moneyfy.data.model.UserDao
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    userDao: UserDao,
    currentLoggedInEmail: String,
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showProfile by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    val scope = rememberCoroutineScope()

    // Load user trực tiếp từ SQLite
    LaunchedEffect(currentLoggedInEmail) {
        scope.launch {
            currentUser = userDao.getUserByEmail(currentLoggedInEmail)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Thanh tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (showProfile) "Thông tin cá nhân" else "Cài đặt",
                color = Color.White,
                fontSize = 22.sp,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showProfile) {
            // ===== Profile =====
            currentUser?.let { user ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1565C0), shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Text("Username: ${user.username}", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: ${user.email}", color = Color.White, fontSize = 18.sp)
                }
            } ?: run {
                Text("Đang tải thông tin...", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showProfile = false },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quay lại Cài đặt", color = Color.White)
            }

        } else {
            // ===== Cài đặt =====
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

            buttonList.forEach { (text, icon, color) ->
                Button(
                    onClick = {
                        when (text) {
                            "Thông tin cá nhân" -> showProfile = true
                            "Đăng xuất" -> onLogout()
                            else -> {}
                        }
                    },
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
                            Icon(icon, contentDescription = text, tint = Color.White, modifier = Modifier.padding(end = 8.dp))
                            Text(text, color = Color.White)
                        }
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = "Mở",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
