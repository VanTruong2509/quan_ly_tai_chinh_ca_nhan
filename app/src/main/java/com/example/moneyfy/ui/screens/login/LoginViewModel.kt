package com.example.moneyfy.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val username = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)

    // Bộ nhớ user tạm
    private val registeredUsers = mutableMapOf<String, String>()

    fun onRegisterClick(onSuccess: () -> Unit) {
        if (username.value.isBlank() || password.value.isBlank() || email.value.isBlank()) {
            errorMessage.value = "Vui lòng nhập đầy đủ thông tin"
            return
        }
        if (registeredUsers.containsKey(username.value)) {
            errorMessage.value = "Tên người dùng đã tồn tại"
            return
        }

        // Lưu user mới
        registeredUsers[username.value] = password.value
        errorMessage.value = null
        onSuccess() // Chuyển sang màn đăng nhập
    }

    fun login(user: String, pass: String): Boolean {
        val savedPass = registeredUsers[user]
        return savedPass != null && savedPass == pass
    }
}
