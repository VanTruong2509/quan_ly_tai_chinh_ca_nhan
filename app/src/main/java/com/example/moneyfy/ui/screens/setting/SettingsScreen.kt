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
    var showCurrency by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    var showRating by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf("Tiếng Việt") }
    var selectedCurrency by remember { mutableStateOf("VND") }

    var currentUser by remember { mutableStateOf<User?>(null) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showOldPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var otpCode by remember { mutableStateOf("") }
    var inputOtp by remember { mutableStateOf("") }

    var feedbackText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(currentLoggedInEmail) {
        currentUser = userDao.getUserByEmail(currentLoggedInEmail)
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->

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
                        showCurrency -> showCurrency = false
                        showFeedback -> showFeedback = false
                        showRating -> showRating = false
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
                        showCurrency -> "Đơn vị tiền tệ"
                        showFeedback -> "Báo lỗi hoặc phản hồi"
                        showRating -> "Đánh giá"
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
                        modifier = Modifier.fillMaxWidth()
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

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    scope.launch {
                        val user = currentUser
                        when {
                            user == null -> snackbarHostState.showSnackbar("Không tìm thấy người dùng")
                            oldPassword != user.password -> snackbarHostState.showSnackbar("Sai mật khẩu")
                            newPassword != confirmPassword -> snackbarHostState.showSnackbar("Mật khẩu không khớp")
                            newPassword.length < 6 -> snackbarHostState.showSnackbar("Mật khẩu ≥ 6 ký tự")
                            else -> {
                                otpCode = Random.nextInt(100000, 999999).toString()
                                snackbarHostState.showSnackbar("OTP: $otpCode")
                                showOtp = true
                                showChangePassword = false
                            }
                        }
                    }
                }) { Text("Tiếp tục") }
            }

            // ===== OTP =====
            else if (showOtp) {
                OutlinedTextField(
                    value = inputOtp,
                    onValueChange = { inputOtp = it },
                    label = { Text("Nhập OTP") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    scope.launch {
                        val user = currentUser
                        if (inputOtp == otpCode && user != null) {
                            userDao.updateUserPassword(user.email, newPassword)
                            snackbarHostState.showSnackbar(" Đổi mật khẩu thành công")
                            onLogout()
                        } else snackbarHostState.showSnackbar("❌ OTP sai")
                    }
                }) { Text("Xác nhận OTP") }
            }

            // ===== NGÔN NGỮ =====
            else if (showLanguage) {
                listOf("Tiếng Việt", "English", "日本語", "한국어").forEach {
                    Button(modifier = Modifier.fillMaxWidth().padding(6.dp), onClick = {
                        selectedLanguage = it
                        scope.launch { snackbarHostState.showSnackbar(" Đã chọn $it") }
                    }) { Text(it) }
                }
            }

            // ===== ĐƠN VỊ TIỀN TỆ =====
            else if (showCurrency) {
                listOf("VND", "USD", "EUR", "JPY").forEach {
                    Button(modifier = Modifier.fillMaxWidth().padding(6.dp), onClick = {
                        selectedCurrency = it
                        scope.launch { snackbarHostState.showSnackbar(" Đã chọn $it") }
                    }) { Text(it) }
                }
            }

            // ===== PHẢN HỒI =====
            else if (showFeedback) {
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    label = { Text("Nhập nội dung phản hồi") },
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(" Gửi phản hồi thành công")
                        feedbackText = ""
                    }
                }) { Text("Gửi phản hồi") }
            }

            // ===== ĐÁNH GIÁ =====
            else if (showRating) {
                Text("Chọn số sao:", color = Color.White)
                Row {
                    repeat(5) { index ->
                        IconButton(onClick = { rating = index + 1 }) {
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = if (index < rating) Color.Yellow else Color.Gray
                            )
                        }
                    }
                }

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(" Cảm ơn bạn đã đánh giá $rating sao!")
                    }
                }) { Text("Gửi đánh giá") }
            }

            // ===== MENU GIỮ NGUYÊN ICON CŨ =====
            else {
                val buttonList = listOf(
                    Triple("Thông tin cá nhân", Icons.Default.Person, Color(0xFF1565C0)),
                    Triple("Đổi mật khẩu", Icons.Default.Lock, Color(0xFF1565C0)),
                    Triple("Ngôn ngữ", Icons.Default.Language, Color(0xFF1565C0)),
                    Triple("Đơn vị tiền tệ", Icons.Default.AttachMoney, Color(0xFF1565C0)),
                    Triple("Báo lỗi hoặc phản hồi", Icons.Default.Chat, Color(0xFF1565C0)),
                    Triple("Đánh giá", Icons.Default.Favorite, Color(0xFF1565C0)),
                    Triple("Đăng xuất", Icons.Default.PowerSettingsNew, Color(0xFFD32F2F))
                )

                buttonList.forEach { (text, icon, color) ->
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = color),
                        onClick = {
                            when (text) {
                                "Thông tin cá nhân" -> showProfile = true
                                "Đổi mật khẩu" -> showChangePassword = true
                                "Ngôn ngữ" -> showLanguage = true
                                "Đơn vị tiền tệ" -> showCurrency = true
                                "Báo lỗi hoặc phản hồi" -> showFeedback = true
                                "Đánh giá" -> showRating = true
                                "Đăng xuất" -> onLogout()
                            }
                        }
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
