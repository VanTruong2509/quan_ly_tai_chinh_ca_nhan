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
fun AddTransactionScreen(navController: NavController) {

    var isIncome by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("Danh mục") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

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

        Spacer(Modifier.height(10.dp))

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
            ) {
                Text("Chi tiêu")
            }

            Button(
                onClick = { isIncome = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIncome) Color.Green else Color.DarkGray
                )
            ) {
                Text("Thu nhập")
            }
        }

        Spacer(Modifier.height(20.dp))

        // ===== FORM =====
        FormItem(
            label = "Danh mục",
            value = category,
            onClick = { navController.navigate("category_list") }
        )

        FormItem(
            label = "VND",
            value = amount,
            onClick = { }
        )

        FormItem(
            label = "Ngày & giờ",
            value = "Hôm nay 7:00",
            onClick = { }
        )

        FormItem(
            label = "Ghi chú",
            value = note,
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
