package com.example.moneyfy.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.example.moneyfy.data.model.User
import com.example.moneyfy.data.model.UserDao
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    val username = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val errorMessage = mutableStateOf("")
    val otpCode = mutableStateOf("")
    var phoneNumber = ""

    // Đăng ký user mới
    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (username.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
                errorMessage.value = "Vui lòng nhập đầy đủ thông tin"
                return@launch
            }

            val existing = userDao.getUserByUsername(username.value)
            if (existing != null) {
                errorMessage.value = "Tên người dùng đã tồn tại"
                return@launch
            }

            userDao.insertUser(
                User(username = username.value, email = email.value, password = password.value)
            )
            errorMessage.value = ""
            onSuccess()
        }
    }

    // Đăng nhập bằng email + password
    fun loginWithEmail(inputEmail: String, inputPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userDao.loginByEmail(inputEmail, inputPassword)
            if (user != null) {
                errorMessage.value = ""
                onResult(true)
            } else {
                errorMessage.value = "Sai email hoặc mật khẩu!"
                onResult(false)
            }
        }
    }

    // Gửi OTP (fake)
    fun sendOtp(phone: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (phone.isBlank()) {
                errorMessage.value = "Vui lòng nhập số điện thoại!"
                onResult(false)
                return@launch
            }

            val otp = (100000..999999).random().toString()
            otpCode.value = otp
            phoneNumber = phone

            onResult(true)
        }
    }

    // Kiểm tra OTP
    fun verifyOtp(input: String): Boolean {
        return input == otpCode.value
    }

    // Cập nhật mật khẩu mới
    fun resetPassword(email: String, newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (newPassword.isBlank()) {
                errorMessage.value = "Mật khẩu mới không được để trống!"
                onResult(false)
                return@launch
            }

            userDao.updatePassword(email, newPassword)
            onResult(true)
        }
    }
}
