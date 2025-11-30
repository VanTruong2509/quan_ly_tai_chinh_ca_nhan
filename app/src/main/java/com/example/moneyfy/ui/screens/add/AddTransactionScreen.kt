package com.example.moneyfy.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTransactionScreen(navController: NavController) {

    var isIncome by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("Danh mục") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    // ⭐ State lưu ngày giờ hiện tại (String)
    var currentDateTime: String by remember { mutableStateOf(getCurrentTimeString()) }

    // ⭐ Cập nhật tự động mỗi phút
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = getCurrentTimeString()
            delay(60000L) // 1 phút
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        // ===== HEADER =====
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Hủy", color = Color.Cyan, fontSize = 18.sp)
            }

            Spacer(Modifier.weight(1f))

            Text(
                "Thêm ghi nhận",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        // ===== TABS =====
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isIncome = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isIncome) Color.Red else Color.DarkGray
                )
            ) { Text("Chi tiêu") }

            Button(
                onClick = { isIncome = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIncome) Color.Green else Color.DarkGray
                )
            ) { Text("Thu nhập") }
        }

        Spacer(Modifier.height(20.dp))

        // ===== FORM =====
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
            isAmount = true,
            onValueChange = { amount = it }
        )


        // ⭐ FormItem ngày giờ, hiển thị thời gian hiện tại
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

        // ===== SAVE BUTTON =====
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8A2BE2))
        ) {
            Text("Lưu", color = Color.White, fontSize = 20.sp)
        }
    }
}

fun getCurrentTimeString(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return "Hôm nay ${sdf.format(Date())}"
}