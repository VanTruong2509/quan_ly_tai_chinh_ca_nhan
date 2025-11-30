package com.example.moneyfy.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Search

@Composable
fun CategoryListScreen(navController: NavController) {

    val categories = listOf(
        CategoryItem(1, "Ăn uống"),
        CategoryItem(2, "Mua sắm"),
        CategoryItem(3, "Nhà ở"),
        CategoryItem(4, "Phương tiện"),
        CategoryItem(5, "Đầu tư"),
        CategoryItem(6, "Chi phí tài chính"),
        CategoryItem(7, "Cuộc sống & Giải trí"),
        CategoryItem(8, "Khác")
    )

    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF002B55))
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text("Danh mục", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(10.dp))

        TextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tìm kiếm") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(50.dp)
        )

        Spacer(Modifier.height(20.dp))

        LazyColumn {
            items(categories.size) { index ->
                val c = categories[index]
                CategoryRow(
                    name = c.name,
                    icon = getCategoryIcon(c.name),
                    onClick = {
                        navController.navigate("sub_category/${c.id}/${c.name}")
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryRow(name: String, icon: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE3F2FD), RoundedCornerShape(10.dp))
            .padding(14.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 22.sp)

        Spacer(modifier = Modifier.width(12.dp))

        Text(name, color = Color.Black, fontSize = 16.sp)

        Spacer(modifier = Modifier.weight(1f))

        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Black)
    }

    Spacer(modifier = Modifier.height(10.dp))
}

fun getCategoryIcon(name: String): String {
    return when (name) {
        "Ăn uống" -> "🍽"
        "Mua sắm" -> "🛒"
        "Nhà ở" -> "🏠"
        "Phương tiện" -> "🚗"
        "Đầu tư" -> "📈"
        "Chi phí tài chính" -> "💰"
        "Cuộc sống & Giải trí" -> "🎉"
        else -> "☰"
    }
}

data class CategoryItem(val id: Int, val name: String)
