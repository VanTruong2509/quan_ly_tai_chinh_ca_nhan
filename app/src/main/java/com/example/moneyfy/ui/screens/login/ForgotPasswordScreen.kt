package com.example.moneyfy.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Gradient và màu button đồng bộ với SignIn
val signInGradient = Brush.verticalGradient(listOf(Color(0xFF0086FF), Color.Black))
val signInButtonColor = Color(0xFF00C853)

// -------------------------------
// 1) MÀN HÌNH QUÊN MẬT KHẨU
// -------------------------------
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var phone by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(signInGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(20.dp)
        ) {
            Text(
                text = "Quên mật khẩu",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số điện thoại") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("verify_otp/$phone") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = signInButtonColor)
            ) {
                Text("Gửi mã OTP", color = Color.White)
            }
        }
    }
}

// -------------------------------
// 2) MÀN HÌNH XÁC THỰC OTP
// -------------------------------
@Composable
fun VerifyOtpScreen(navController: NavController, phone: String) {
    var otp by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(signInGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(20.dp)
        ) {
            Text(
                text = "Nhập mã OTP",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Spacer(Modifier.height(8.dp))
            Text(text = "Mã OTP đã gửi đến: $phone", color = Color.Gray)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("OTP") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("reset_password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = signInButtonColor)
            ) {
                Text("Xác thực", color = Color.White)
            }
        }
    }
}

// -------------------------------
// 3) MÀN HÌNH ĐẶT LẠI MẬT KHẨU
// -------------------------------
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(signInGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(20.dp)
        ) {
            Text(
                text = "Đặt lại mật khẩu",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Mật khẩu mới") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Nhập lại mật khẩu") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = signInButtonColor)
            ) {
                Text("Xác nhận đổi mật khẩu", color = Color.White)
            }
        }
    }
}
