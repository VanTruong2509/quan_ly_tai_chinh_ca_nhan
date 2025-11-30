package com.example.moneyfy.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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

@Composable
fun SignInScreen(navController: NavController, viewModel: LoginViewModel) {
    val gradient = Brush.verticalGradient(listOf(Color(0xFF0086FF), Color.Black))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Đăng nhập",
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                }
            }
        )

        // 👉 Quên mật khẩu
        Text(
            text = "Quên mật khẩu?",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
                .clickable {
                    navController.navigate("forgot_password")
                }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.loginWithEmail(email, password) { success ->
                    if (success) {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        message = viewModel.errorMessage.value
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text("Đăng nhập")
        }

        if (message.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.Red)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Đăng ký",
            color = Color.White,
            modifier = Modifier.clickable { navController.navigate("register") }
        )
    }
}
