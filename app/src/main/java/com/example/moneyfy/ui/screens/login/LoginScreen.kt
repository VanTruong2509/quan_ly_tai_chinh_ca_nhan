package com.example.moneyfy.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moneyfy.R

@Composable
fun LoginScreen(navController: NavController) {
    // Gradient nền màn hình
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0086FF), Color.Black)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 🖼️ Logo app
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        // Nút đăng nhập bằng Email
        Button(
            onClick = { navController.navigate("signin") }, // điều hướng sang màn hình SignIn
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text(text = "Đăng nhập bằng Email", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Hoặc", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Đăng ký bên dưới để tạo tài khoản bảo mật.",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🧩 Hàng icon mạng xã hội
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialIcon(R.drawable.fb, "Facebook")
            SocialIcon(R.drawable.gg, "Google")
            SocialIcon(R.drawable.ap, "Apple")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Link Đăng ký
        Text(
            text = "Đăng ký",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate("register") // điều hướng sang màn hình đăng ký
            }
        )
    }
}

@Composable
fun SocialIcon(iconRes: Int, name: String) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .size(56.dp)
            .clickable { /* TODO: Xử lý đăng nhập mạng xã hội */ }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = name,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
