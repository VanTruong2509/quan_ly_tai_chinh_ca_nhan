package com.example.moneyfy.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moneyfy.ui.screens.home.HomeViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddExpenseScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    selectedCategory: String? = null
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(selectedCategory ?: "Danh mục") }

    var currentDateTime by remember { mutableStateOf(getCurrentDateTime()) }

    // Cập nhật thời gian tự động mỗi giây
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = getCurrentDateTime()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        // HEADER
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

        // TABS Chi tiêu / Thu nhập
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* xử lý nếu muốn */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Chi tiêu") }

            Button(
                onClick = { /* xử lý nếu muốn */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) { Text("Thu nhập") }
        }

        Spacer(Modifier.height(20.dp))

        // DANH MỤC
        FormItem(
            label = "Danh mục",
            value = category,
            icon = Icons.Default.Category,
            onClick = { navController.navigate("category_list") }
        )

        Spacer(Modifier.height(8.dp))

        // SỐ TIỀN
        AmountInput(amount = amount, onAmountChange = { amount = it })

        Spacer(Modifier.height(12.dp))

        // NGÀY GIỜ
        FormItem(
            label = "Ngày & giờ",
            value = currentDateTime,
            icon = Icons.Default.AccessTime,
            onClick = {}
        )

        // GHI CHÚ
        FormItem(
            label = "Ghi chú",
            value = note,
            icon = Icons.Default.Note,
            onClick = {}
        )

        Spacer(Modifier.weight(1f))

        // NÚT LƯU
        Button(
            onClick = {
                val amt = amount.toFloatOrNull() ?: 0f
                if (amt > 0f) {
                    homeViewModel.addSpending(
                        amount = amt,
                        category = category,
                        note = note
                    )
                }
                navController.popBackStack("home", inclusive = false)
            },
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

@Composable
fun AmountInput(amount: String, onAmountChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0080FF), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        BasicTextField(
            value = amount,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) onAmountChange(newValue)
            },
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                if (amount.isEmpty()) {
                    Text("VNĐ", color = Color.White.copy(alpha = 0.5f))
                }
                innerTextField()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
