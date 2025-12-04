package com.example.moneyfy.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyfy.data.model.User
import com.example.moneyfy.data.model.UserDao
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SettingsScreen(
    userDao: UserDao,
    currentLoggedInEmail: String,
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showProfile by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    var showOtp by remember { mutableStateOf(false) }
    var showLanguage by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf("Tiếng Việt") }

    var currentUser by remember { mutableStateOf<User?>(null) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showOldPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var otpCode by remember { mutableStateOf("") }
    var inputOtp by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(currentLoggedInEmail) {
        currentUser = userDao.getUserByEmail(currentLoggedInEmail)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {

            // ===== HEADER =====
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    when {
                        showProfile -> showProfile = false
                        showChangePassword -> showChangePassword = false
                        showOtp -> showOtp = false
                        showLanguage -> showLanguage = false
                        else -> onBackClick()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Text(
                    text = when {
                        showProfile -> "Thông tin cá nhân"
                        showChangePassword -> "Đổi mật khẩu"
                        showOtp -> "Xác thực OTP"
                        showLanguage -> "Ngôn ngữ"
                        else -> "Cài đặt"
                    },
                    color = Color.White,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== PROFILE =====
            if (showProfile) {
                currentUser?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1565C0))
                            .padding(16.dp)
                    ) {
                        Text("Username: ${user.username}", color = Color.White)
                        Text("Email: ${user.email}", color = Color.White)
                    }
                }
            }

            // ===== ĐỔI MẬT KHẨU =====
            else if (showChangePassword) {

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Mật khẩu cũ") },
                    visualTransformation = if (showOldPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showOldPassword = !showOldPassword }) {
                            Icon(if (showOldPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showNewPassword = !showNewPassword }) {
                            Icon(if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Xác nhận mật khẩu") },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            val user = currentUser

                            when {
                                user == null ->
                                    snackbarHostState.showSnackbar("Không tìm thấy người dùng")

                                oldPassword != user.password ->
                                    snackbarHostState.showSnackbar("Mật khẩu cũ sai")

                                newPassword != confirmPassword ->
                                    snackbarHostState.showSnackbar("Mật khẩu không khớp")

                                newPassword.length < 6 ->
                                    snackbarHostState.showSnackbar("Mật khẩu phải ≥ 6 ký tự")

                                else -> {
                                    otpCode = Random.nextInt(100000, 999999).toString()
                                    snackbarHostState.showSnackbar("OTP của bạn: $otpCode")
                                    showOtp = true
                                    showChangePassword = false
                                }
                            }
                        }
                    }
                ) {
                    Text("Tiếp tục")
                }
            }

            // ===== OTP =====
            else if (showOtp) {

                OutlinedTextField(
                    value = inputOtp,
                    onValueChange = { inputOtp = it },
                    label = { Text("Nhập mã OTP") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            val user = currentUser

                            if (inputOtp == otpCode && user != null) {
                                userDao.updateUserPassword(user.email, newPassword)
                                snackbarHostState.showSnackbar("✅ Đổi mật khẩu thành công")
                                onLogout()
                            } else {
                                snackbarHostState.showSnackbar("❌ OTP không đúng")
                            }
                        }
                    }
                ) {
                    Text("Xác nhận OTP")
                }
            }

            // ===== ĐỔI NGÔN NGỮ =====
            else if (showLanguage) {

                val languages = listOf("Tiếng Việt", "English", "日本語", "한국어")

                languages.forEach { lang ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (lang == selectedLanguage)
                                Color(0xFF2E7D32)
                            else
                                Color(0xFF1565C0)
                        ),
                        onClick = {
                            selectedLanguage = lang
                            scope.launch {
                                snackbarHostState.showSnackbar("✅ Đã chọn $lang")
                            }
                        }
                    ) {
                        Text(lang)
                    }
                }
            }

            // ===== MENU ĐẦY ĐỦ (KHÔNG MẤT CHỨC NĂNG) =====
            else {
                val buttonList = listOf(
                    Triple("Thông tin cá nhân", Icons.Default.Person, Color(0xFF1565C0)),
                    Triple("Đổi mật khẩu", Icons.Default.Lock, Color(0xFF1565C0)),
                    Triple("Ngôn ngữ", Icons.Default.Language, Color(0xFF1565C0)),
                    Triple("Đơn vị tiền tệ", Icons.Default.AttachMoney, Color(0xFF1565C0)),
                    Triple("Các tài khoản", Icons.Default.AccountBox, Color(0xFF1565C0)),
                    Triple("Báo lỗi hoặc phản hồi", Icons.Default.Chat, Color(0xFF1565C0)),
                    Triple("Đánh giá", Icons.Default.Favorite, Color(0xFF1565C0)),
                    Triple("Đăng xuất", Icons.Default.PowerSettingsNew, Color(0xFFD32F2F))
                )

                buttonList.forEach { (text, icon, color) ->
                    Button(
                        onClick = {
                            when (text) {
                                "Thông tin cá nhân" -> showProfile = true
                                "Đổi mật khẩu" -> showChangePassword = true
                                "Ngôn ngữ" -> showLanguage = true
                                "Đăng xuất" -> onLogout()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Icon(icon, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text)
                            }
                            Icon(Icons.Default.ArrowForwardIos, null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}
