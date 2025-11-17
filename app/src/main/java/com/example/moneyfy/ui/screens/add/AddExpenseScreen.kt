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

@Composable
fun AddExpenseScreen(navController: NavController) {

    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Danh mục") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

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

        FormItem("Danh mục", category) { navController.navigate("category_list") }
        FormItem("VNĐ", amount) {}
        FormItem("Ngày & giờ", "Hôm nay 7:00") {}
        FormItem("Ghi chú", note) {}

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8A2BE2))
        ) {
            Text("Lưu", fontSize = 20.sp, color = Color.White)
        }
    }
}
