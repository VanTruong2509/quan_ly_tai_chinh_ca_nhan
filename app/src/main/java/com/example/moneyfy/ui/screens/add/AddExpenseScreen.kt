package com.example.moneyfy.ui.screens.add

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddExpenseScreen(
    navController: NavController,
    selectedCategory: String? = null
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(selectedCategory ?: "Danh mục") }

    // ⭐ State lưu ngày giờ hiện tại
    var currentDateTime by remember { mutableStateOf(getCurrentDateTime()) }

    // ⭐ Cập nhật liên tục từng giây (hoặc từng phút nếu muốn)
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = getCurrentDateTime()
            kotlinx.coroutines.delay(1000) // 1 giây
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Hủy", color = Color.Cyan, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Thêm ghi nhận",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Chi tiêu") }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) { Text("Thu nhập") }
        }

        Spacer(Modifier.height(20.dp))

        // ==== FORM ITEMS ====
        FormItem(
            label = "Danh mục",
            value = category,
            icon = Icons.Default.Category,
            onClick = { navController.navigate("category_list") }
        )

        FormItem(
            label = "VNĐ",
            value = amount,
            icon = Icons.Default.AttachMoney,
            onClick = { }
        )

        // ⭐ Hiển thị ngày giờ thực sự
        FormItem(
            label = "Ngày & giờ",
            value = currentDateTime,
            icon = Icons.Default.AccessTime,
            onClick = { }
        )

        FormItem(
            label = "Ghi chú",
            value = note,
            icon = Icons.Default.Note,
            onClick = { }
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8A2BE2))
        ) {
            Text("Lưu", fontSize = 20.sp, color = Color.White)
        }
    }
}

// Lấy ngày giờ hiện tại theo định dạng "dd/MM/yyyy HH:mm:ss"
fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
