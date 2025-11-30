package com.example.moneyfy.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: LoginViewModel) {
    var email by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var generatedOtp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0086FF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quên mật khẩu", fontSize = 28.sp, color = Color.White)

        Spacer(Modifier.height(16.dp))

        if (!otpSent) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Nhập email của bạn", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        message = "Vui lòng nhập email"
                        return@Button
                    }
                    viewModel.sendOtpToEmail(email) { success, msg, otp ->
                        message = msg
                        if (success && otp != null) {
                            otpSent = true
                            generatedOtp = otp
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
            ) {
                Text("Tạo OTP", color = Color.White)
            }
        } else {
            OutlinedTextField(
                value = otpInput,
                onValueChange = { otpInput = it },
                label = { Text("Nhập OTP", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Mật khẩu mới", color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (otpInput != generatedOtp) {
                        message = "OTP không đúng"
                        return@Button
                    }
                    if (newPassword.isBlank()) {
                        message = "Vui lòng nhập mật khẩu mới"
                        return@Button
                    }
                    viewModel.updatePassword(email, newPassword) { success, msg ->
                        message = msg
                        if (success) {
                            otpSent = false
                            email = ""
                            otpInput = ""
                            newPassword = ""

                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
            ) {
                Text("Cập nhật mật khẩu", color = Color.White)
            }
        }

        if (message.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
            Text(
                message,
                color = if (message.contains("không") || message.contains("OTP")) Color.Red else Color.Green
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Quay lại đăng nhập",
            color = Color.White,
            modifier = Modifier.clickable { navController.popBackStack() }
        )
    }
}
