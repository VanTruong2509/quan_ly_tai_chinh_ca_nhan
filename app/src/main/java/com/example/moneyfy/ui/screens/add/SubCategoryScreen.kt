package com.example.moneyfy.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SubCategoryScreen(
    navController: NavController,
    categoryId: Int,
    categoryName: String
) {
    // icon theo danh mục chính
    val mainIcon: ImageVector = when (categoryId) {
        1 -> Icons.Default.Restaurant
        2 -> Icons.Default.ShoppingBag
        3 -> Icons.Default.Home
        4 -> Icons.Default.DirectionsCar
        else -> Icons.Default.Category
    }

    // icon cho subcategory
    fun subIcon(index: Int): ImageVector {
        return when (index) {
            0 -> Icons.Default.CheckCircle
            1 -> Icons.Default.Eco
            2 -> Icons.Default.LocalDining
            else -> Icons.Default.Coffee
        }
    }

    val subCategories = when (categoryId) {
        1 -> listOf( "Thực phẩm", "Nhà hàng, đồ ăn nhanh", "Quán bar, quán cà phê")
        2 -> listOf("Quần áo", "Phụ kiện", "Mỹ phẩm")
        3 -> listOf("Tiền nhà", "Điện nước", "Internet")
        else -> listOf("Khác 1", "Khác 2")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // ===== HEADER =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "< Danh mục",
                color = Color.White,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Text(
                categoryName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Chỉnh sửa",
                color = Color.White
            )
        }

        Spacer(Modifier.height(10.dp))

        // ===== Ô DANH MỤC CHÍNH =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF005DAA))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(mainIcon, contentDescription = null, tint = Color.White)

            Spacer(Modifier.width(10.dp))

            Text(categoryName, color = Color.White, fontSize = 18.sp)
        }

        Spacer(Modifier.height(18.dp))

        // ===== TITLE =====
        Text(
            "PHÂN LOẠI PHỤ",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(Modifier.height(10.dp))

        // ===== LIST SUBCATEGORY =====
        LazyColumn {
            items(subCategories.size) { index ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("add_expense?category=${subCategories[index]}") {
                                popUpTo("add_expense") { inclusive = true }
                            }
                        }
                        .background(Color(0xFF0074C7))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        subIcon(index),                      // icon phụ
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        subCategories[index],
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

